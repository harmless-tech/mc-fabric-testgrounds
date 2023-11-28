package tech.harmless.mc.fabric.testgrounds.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;

public class ItemLooseGunpowderClump extends Item {

    public static final ItemLooseGunpowderClump LOOSE_GUNPOWDER_CLUMP =
            new ItemLooseGunpowderClump();

    public ItemLooseGunpowderClump() {
        super(new FabricItemSettings());
    }
}
