package tech.harmless.mc.fabric.testgrounds.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;

public class ItemSoakedGunpowderCube extends Item {

    public static final ItemSoakedGunpowderCube SOAKED_GUNPOWDER_CUBE =
            new ItemSoakedGunpowderCube();

    public ItemSoakedGunpowderCube() {
        super(new FabricItemSettings());
    }
}
