package tech.harmless.mc.fabric.testgrounds;

import static net.minecraft.server.command.CommandManager.literal;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.harmless.mc.fabric.testgrounds.blocks.BlockCompressedGunpowder;
import tech.harmless.mc.fabric.testgrounds.items.ItemLooseGunpowderClump;
import tech.harmless.mc.fabric.testgrounds.items.ItemSoakedGunpowderCube;
import tech.harmless.mc.fabric.testgrounds.items.rockets.Rocket;

public class Testgrounds implements ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("testgrounds");

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        LOGGER.info("Hello Fabric world!");

        Registry.register(
                Registries.ITEM,
                new Identifier("testgrounds", "loose_gunpowder_clump"),
                ItemLooseGunpowderClump.LOOSE_GUNPOWDER_CLUMP);

        Registry.register(
                Registries.ITEM,
                new Identifier("testgrounds", "soaked_gunpowder_cube"),
                ItemSoakedGunpowderCube.SOAKED_GUNPOWDER_CUBE);

        Registry.register(
                Registries.BLOCK,
                new Identifier("testgrounds", "compressed_gunpowder_block"),
                BlockCompressedGunpowder.BLOCK_COMPRESSED_GUNPOWDER);
        Registry.register(
                Registries.ITEM,
                new Identifier("testgrounds", "compressed_gunpowder_block"),
                new BlockItem(
                        BlockCompressedGunpowder.BLOCK_COMPRESSED_GUNPOWDER,
                        new FabricItemSettings()));

        Registry.register(Registries.ITEM, new Identifier("testgrounds", "rocket"), Rocket.ROCKET);

        Registry.register(
                Registries.ITEM_GROUP,
                new Identifier("testgrounds", "t_group"),
                TGRegistry.ITEM_GROUP);

        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess, environment) ->
                        dispatcher.register(
                                literal("nbt")
                                        .executes(
                                                context -> {
                                                    var player = context.getSource().getPlayer();
                                                    if (player != null) {
                                                        var main =
                                                                player.getInventory()
                                                                        .getMainHandStack();
                                                        if (main != null
                                                                && main.getNbt() != null
                                                                && main.getNbt().getKeys()
                                                                        != null) {
                                                            for (var key :
                                                                    main.getNbt().getKeys()) {
                                                                context.getSource()
                                                                        .sendFeedback(
                                                                                () ->
                                                                                        Text
                                                                                                .literal(
                                                                                                        main.getName()
                                                                                                                        .getString()
                                                                                                                + "::"
                                                                                                                + key
                                                                                                                + " = "
                                                                                                                + main.getNbt()
                                                                                                                        .get(
                                                                                                                                key)
                                                                                                                        .asString()),
                                                                                false);
                                                            }
                                                            EnchantmentHelper.get(main)
                                                                    .forEach(
                                                                            (en, i) -> {
                                                                                context.getSource()
                                                                                        .sendFeedback(
                                                                                                () ->
                                                                                                        Text
                                                                                                                .literal(
                                                                                                                        "EN!!"
                                                                                                                                + main.getName()
                                                                                                                                        .getString()
                                                                                                                                + "::"
                                                                                                                                + en.getName(
                                                                                                                                                0)
                                                                                                                                        .getString()
                                                                                                                                + " = "
                                                                                                                                + i),
                                                                                                false);
                                                                            });
                                                        }
                                                        return 1;
                                                    } else {
                                                        context.getSource()
                                                                .sendFeedback(
                                                                        () ->
                                                                                Text.literal(
                                                                                        "Only players"
                                                                                            + " can use"
                                                                                            + " /nbt"),
                                                                        false);
                                                        return -1;
                                                    }
                                                })));
    }
}
