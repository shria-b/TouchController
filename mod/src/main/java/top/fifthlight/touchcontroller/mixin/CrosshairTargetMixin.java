package top.fifthlight.touchcontroller.mixin;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public abstract class CrosshairTargetMixin {
    @Invoker("ensureTargetInRange")
    private static HitResult ensureTargetInRange(HitResult hitResult, Vec3d cameraPos, double interactionRange) {
        throw new AssertionError();
    }

    @Inject(method = "findCrosshairTarget", at = @At("HEAD"), cancellable = true)
    private void findCrosshairTarget(Entity camera, double blockInteractionRange, double entityInteractionRange, float tickDelta, CallbackInfoReturnable<HitResult> cir) {
        // var crosshairStatus = CrosshairStateModel.INSTANCE.getState().getStatus();
    }
}
