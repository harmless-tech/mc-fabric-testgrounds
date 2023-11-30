package tech.harmless.mc.fabric.testgrounds;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import tech.harmless.mc.fabric.testgrounds.blocks.BlockCompressedGunpowder;
import tech.harmless.mc.fabric.testgrounds.items.ItemLooseGunpowderClump;
import tech.harmless.mc.fabric.testgrounds.items.ItemSoakedGunpowderCube;

// TODO: This class should handle adding all blocks and items!

public final class TGRegistry {
    public static final ItemGroup ITEM_GROUP =
            FabricItemGroup.builder()
                    .icon(() -> new ItemStack(BlockCompressedGunpowder.BLOCK_COMPRESSED_GUNPOWDER))
                    .displayName(Text.translatable("itemGroup.testgrounds.main"))
                    .entries(
                            ((displayContext, entries) -> {
                                entries.add(ItemLooseGunpowderClump.LOOSE_GUNPOWDER_CLUMP);
                                entries.add(ItemSoakedGunpowderCube.SOAKED_GUNPOWDER_CUBE);
                                entries.add(BlockCompressedGunpowder.BLOCK_COMPRESSED_GUNPOWDER);
                            }))
                    .build();
}
