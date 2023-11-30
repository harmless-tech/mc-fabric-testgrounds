package tech.harmless.mc.fabric.testgrounds.mixin;

import net.minecraft.nbt.NbtCompound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NbtCompound.class)
public class ExampleMixin {

    private static final Logger LOGGER = LoggerFactory.getLogger("testgrounds");

    @Inject(at = @At("RETURN"), method = "getByte")
    private void init(String key, CallbackInfoReturnable<Byte> cir) {
        if ("Flight".equals(key)) {
            LOGGER.info("Flight Byte: " + cir.getReturnValueB());
        }
    }
}
