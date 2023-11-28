package tech.harmless.mc.fabric.testgrounds.blocks;

import java.util.List;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.SilkTouchEnchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// TODO: Get BlockItem!
public class BlockCompressedGunpowder extends Block {

    public static final Block BLOCK_COMPRESSED_GUNPOWDER = new BlockCompressedGunpowder();

    public BlockCompressedGunpowder() {
        super(BlockCompressedGunpowder.settings());
    }

    @NotNull private static FabricBlockSettings settings() {
        return FabricBlockSettings.copyOf(Blocks.TNT);
    }

    private void explode(World world, BlockPos pos) {
        if (!world.isClient) {
            world.removeBlock(pos, false);
            world.createExplosion(
                    null, pos.getX(), pos.getY(), pos.getZ(), 8, World.ExplosionSourceType.BLOCK);
        }
    }

    private boolean fireBlock(@NotNull BlockState eState) {
        return eState.isOf(Blocks.FIRE)
                || eState.isOf(Blocks.SOUL_FIRE)
                || eState.isOf(Blocks.LAVA)
                || eState.isOf(Blocks.MAGMA_BLOCK)
                || eState.isOf(Blocks.CAMPFIRE)
                || eState.isOf(Blocks.SOUL_CAMPFIRE);
    }

    @Override
    public void onPlaced(
            World world,
            BlockPos pos,
            BlockState state,
            @Nullable LivingEntity placer,
            ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);

        Vec3i[] positions =
                new Vec3i[] {
                    new Vec3i(1, 0, 0), new Vec3i(-1, 0, 0),
                    new Vec3i(0, 1, 0), new Vec3i(0, -1, 0),
                    new Vec3i(0, 0, 1), new Vec3i(0, 0, -1),
                };

        for (Vec3i p : positions) {
            BlockState eState = world.getBlockState(pos.add(p));
            if (eState != null && fireBlock(eState)) {
                explode(world, pos);
                break;
            }
        }
    }

    @Override
    public void neighborUpdate(
            BlockState state,
            World world,
            BlockPos pos,
            Block sourceBlock,
            BlockPos sourcePos,
            boolean notify) {
        super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify);

        BlockState eState = world.getBlockState(sourcePos);
        if (eState != null && fireBlock(eState)) explode(world, pos);
    }

    @Override
    public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
        super.onDestroyedByExplosion(world, pos, explosion);

        if (!world.isClient) explode(world, pos);
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
                    if (en instanceof SilkTouchEnchantment) {
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

        tooltip.add(Text.translatable("block.testgrounds.compressed_gunpowder_block.tooltip"));
    }
}
