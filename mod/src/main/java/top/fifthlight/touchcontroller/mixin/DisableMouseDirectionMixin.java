package top.fifthlight.touchcontroller.mixin;

import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
abstract class DisableMouseDirectionMixin {
    @Inject(
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/MinecraftClient;player:Lnet/minecraft/client/network/ClientPlayerEntity;",
                    ordinal = 1
            ),
            method = "updateMouse",
            cancellable = true
    )
    private void updateMouse(double timeDelta, CallbackInfo ci) {
        ci.cancel();
    }
}