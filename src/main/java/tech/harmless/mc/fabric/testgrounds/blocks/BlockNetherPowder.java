package tech.harmless.mc.fabric.testgrounds.blocks;

import java.util.List;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.harmless.mc.fabric.testgrounds.Config;

// TODO: Get BlockItem!
public class BlockNetherPowder extends FallingBlock {

    public static final Block BLOCK_NETHER_POWDER = new BlockNetherPowder();

    private static final Identifier ALLOWED_DIM = new Identifier("minecraft", "the_nether");
    private static final Vec3i[] TEST_POSITIONS =
            new Vec3i[] { // TODO: Include adjacent positions?
                new Vec3i(1, 0, 0), new Vec3i(-1, 0, 0),
                new Vec3i(0, 1, 0), new Vec3i(0, -1, 0),
                new Vec3i(0, 0, 1), new Vec3i(0, 0, -1),
            };

    public BlockNetherPowder() {
        super(BlockNetherPowder.settings());
    }

    @NotNull private static FabricBlockSettings settings() {
        return FabricBlockSettings.copyOf(Blocks.SAND);
    }

    private void explode(World world, BlockPos pos) {
        if (!world.isClient) {
            world.removeBlock(pos, false);
            world.createExplosion(
                    null,
                    pos.getX(),
                    pos.getY(),
                    pos.getZ(),
                    Config.NETHER_POWDER_BLOCK_EXPLOSION_POWER,
                    World.ExplosionSourceType.BLOCK);
        }
    }

    private boolean fireBlock(@NotNull BlockState eState) {
        return eState.isOf(Blocks.LAVA)
                || eState.isOf(Blocks.MAGMA_BLOCK)
                || eState.isOf(Blocks.FIRE)
                || eState.isOf(Blocks.SOUL_FIRE)
                || eState.isOf(Blocks.CAMPFIRE)
                || eState.isOf(Blocks.SOUL_CAMPFIRE)
                || eState.isOf(Blocks.TORCH)
                || eState.isOf(Blocks.WALL_TORCH)
                || eState.isOf(Blocks.SOUL_TORCH)
                || eState.isOf(Blocks.SOUL_WALL_TORCH);
    }

    @Override
    public void onPlaced(
            World world,
            BlockPos pos,
            BlockState state,
            @Nullable LivingEntity placer,
            ItemStack itemStack) {
        testExplode(world, pos);
        super.onPlaced(world, pos, state, placer, itemStack);
    }

    @Override
    public void onBlockAdded(
            BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        testExplode(world, pos);
        super.onBlockAdded(state, world, pos, oldState, notify);
    }

    @Override
    public void neighborUpdate(
            BlockState state,
            World world,
            BlockPos pos,
            Block sourceBlock,
            BlockPos sourcePos,
            boolean notify) {
        if (!world.isClient()) {
            BlockState eState = world.getBlockState(sourcePos);
            if (eState != null && fireBlock(eState)) explode(world, pos);
        }

        super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify);
    }

    private void testExplode(World world, BlockPos pos) {
        if (!world.isClient() && !world.getDimensionKey().getValue().equals(ALLOWED_DIM)) {
            var temp = world.getBiome(pos).value().getTemperature();
            if (temp >= 2f) {
                explode(world, pos);
            } else {
                for (Vec3i p : TEST_POSITIONS) {
                    BlockState eState = world.getBlockState(pos.add(p));
                    if (eState != null && fireBlock(eState)) {
                        explode(world, pos);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void onEntityLand(BlockView world, Entity entity) {
        if (!entity.getWorld().isClient() && entity.isOnGround()) {
            if (entity.isOnFire()) {
                explodeView(entity);
            }

            if (entity.isPlayer()) {
                var player = (PlayerEntity) entity;
                if (player.isCreative()) return;

                // Check for feather falling
                boolean feather = false;
                for (var arm : player.getArmorItems()) {
                    for (var en : EnchantmentHelper.get(arm).keySet()) {
                        if (en.equals(Enchantments.FEATHER_FALLING)) {
                            feather = true;
                            break;
                        }
                    }
                }
                if (feather) return;
            }

            // Check for slow falling
            if (entity instanceof LivingEntity e) {
                boolean slowFall = false;
                for (var eff : e.getStatusEffects()) {
                    if (eff.getEffectType().equals(StatusEffects.SLOW_FALLING)) {
                        slowFall = true;
                        break;
                    }
                }
                if (slowFall) return;
            }

            explodeView(entity);
        }

        super.onEntityLand(world, entity);
    }

    private void explodeView(Entity entity) {
        var opt = entity.supportingBlockPos;
        if (opt.isPresent()) {
            var pos = opt.get();
            explode(entity.getWorld(), pos);
        }
    }

    @Override
    public void onProjectileHit(
            World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
        if (!world.isClient()) {
            explode(world, hit.getBlockPos());
        }

        super.onProjectileHit(world, state, hit, projectile);
    }

    @Override
    public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
        if (!world.isClient()) explode(world, pos);
        super.onDestroyedByExplosion(world, pos, explosion);
    }

    @Override
    public boolean shouldDropItemsOnExplosion(Explosion explosion) {
        return false;
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient() && !player.isCreative()) {
            boolean silk = false;
            var item = player.getInventory().getMainHandStack();
            if (item != null) {
                for (var en : EnchantmentHelper.get(item).keySet()) {
                    if (en.equals(Enchantments.SILK_TOUCH)) {
                        silk = true;
                        break;
                    }
                }
            }

            if (!silk) {
                explode(world, pos);
            }
        }

        super.onBreak(world, pos, state, player);
    }

    @Override
    public void appendTooltip(
            ItemStack stack,
            @Nullable BlockView world,
            List<Text> tooltip,
            TooltipContext options) {
        super.appendTooltip(stack, world, tooltip, options);
        tooltip.add(Text.translatable("block.testgrounds.nether_powder_block.tooltip"));
    }
}
