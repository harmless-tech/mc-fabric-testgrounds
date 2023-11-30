package tech.harmless.mc.fabric.testgrounds;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import tech.harmless.mc.fabric.testgrounds.blocks.BlockNetherPowder;
import tech.harmless.mc.fabric.testgrounds.items.ItemDampNetherPowder;
import tech.harmless.mc.fabric.testgrounds.items.ItemLooseGunpowderClump;

// TODO: This class should handle adding all blocks and items!

public final class TGRegistry {
    public static final ItemGroup ITEM_GROUP =
            FabricItemGroup.builder()
                    .icon(() -> new ItemStack(BlockNetherPowder.BLOCK_NETHER_POWDER))
                    .displayName(Text.translatable("itemGroup.testgrounds.main"))
                    .entries(
                            ((displayContext, entries) -> {
                                entries.add(ItemLooseGunpowderClump.LOOSE_GUNPOWDER_CLUMP);
                                entries.add(ItemDampNetherPowder.DAMP_NETHER_POWDER);
                                entries.add(BlockNetherPowder.BLOCK_NETHER_POWDER);
                            }))
                    .build();
}
