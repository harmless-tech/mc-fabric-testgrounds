package tech.harmless.mc.fabric.testgrounds;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import tech.harmless.mc.fabric.testgrounds.blocks.BlockCompressedGunpowder;
import tech.harmless.mc.fabric.testgrounds.items.ItemLooseGunpowderClump;
import tech.harmless.mc.fabric.testgrounds.items.ItemSoakedGunpowderCube;
import tech.harmless.mc.fabric.testgrounds.items.StaticItems;

public final class TGRegistry {
    public static final ItemGroup ITEM_GROUP =
            FabricItemGroup.builder()
                    .icon(() -> new ItemStack(Blocks.SPRUCE_SAPLING))
                    .displayName(Text.translatable("itemGroup.testgrounds.main"))
                    .entries(
                            ((displayContext, entries) -> {
                                entries.add(StaticItems.ITEM_TEST);
                                entries.add(ItemLooseGunpowderClump.LOOSE_GUNPOWDER_CLUMP);
                                entries.add(ItemSoakedGunpowderCube.SOAKED_GUNPOWDER_CUBE);
                                entries.add(BlockCompressedGunpowder.BLOCK_COMPRESSED_GUNPOWDER);
                            }))
                    .build();
}
