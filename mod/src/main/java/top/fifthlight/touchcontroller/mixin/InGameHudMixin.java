package top.fifthlight.touchcontroller.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.fifthlight.touchcontroller.event.HudRenderCallback;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    public void renderCrosshair(DrawContext drawContext, RenderTickCounter tickCounter, CallbackInfo callbackInfo) {
        boolean shouldRender = HudRenderCallback.INSTANCE.getCROSSHAIR().invoker().onCrosshairRender(drawContext, tickCounter);
        if (!shouldRender) {
            callbackInfo.cancel();
        }
    }
}