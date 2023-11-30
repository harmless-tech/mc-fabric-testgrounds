package tech.harmless.mc.fabric.testgrounds.items;

import java.util.List;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ItemSoakedGunpowderCube extends Item {

    public static final ItemSoakedGunpowderCube SOAKED_GUNPOWDER_CUBE =
            new ItemSoakedGunpowderCube();

    public ItemSoakedGunpowderCube() {
        super(new FabricItemSettings());
    }

    @Override
    public void appendTooltip(
            ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(Text.translatable("item.testgrounds.soaked_gunpowder_cube.tooltip"));
    }
}
