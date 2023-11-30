package tech.harmless.mc.fabric.testgrounds.items.rockets;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class Rocket extends Item {

    public static final Rocket ROCKET = new Rocket();

    private static final ItemStack fireworkData;

    static {
        fireworkData = new ItemStack(Items.FIREWORK_ROCKET);
        FireworkRocketItem.setFlight(fireworkData, Byte.MAX_VALUE);
    }

    public Rocket() {
        super(new FabricItemSettings().maxCount(1));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (user.isFallFlying()) {
            if (!world.isClient()) {
                ItemStack stack = user.getStackInHand(hand); // TODO!

                RocketEntity entity = new RocketEntity(world, fireworkData, user);
                world.spawnEntity(entity);

                // TODO: Handle logic

                user.incrementStat(Stats.USED.getOrCreateStat(this));
            }
        }

        return TypedActionResult.pass(user.getStackInHand(hand));
    }
}
