package tech.harmless.mc.fabric.testgrounds.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class StaticItems {
    public static final Item ITEM_TEST = new Item(new FabricItemSettings());

    public static void init() {
        Registry.register(Registries.ITEM, new Identifier("testgrounds", "test_item"), ITEM_TEST);
    }
}
