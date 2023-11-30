package tech.harmless.mc.fabric.testgrounds.items.rockets;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class RocketEntity extends FireworkRocketEntity {
    public RocketEntity(World world, ItemStack stack, LivingEntity shooter) {
        super(world, stack, shooter);
    }
}
