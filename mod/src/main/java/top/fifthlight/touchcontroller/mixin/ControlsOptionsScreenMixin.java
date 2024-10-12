package top.fifthlight.touchcontroller.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.option.ControlsOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.fifthlight.touchcontroller.asset.Texts;
import top.fifthlight.touchcontroller.config.TouchControllerConfigScreen;

@Mixin(ControlsOptionsScreen.class)
public abstract class ControlsOptionsScreenMixin {
    @Inject(at = @At("TAIL"), method = "addOptions")
    protected void addOptions(CallbackInfo ci) {
        var client = MinecraftClient.getInstance();
        var screen = (ControlsOptionsScreen) (Object) this;
        var body = ((GameOptionsScreenAccessor) this).body();
        body.addWidgetEntry(ButtonWidget.builder(Texts.INSTANCE.getOPTIONS_SCREEN(), btn -> client.setScreen(new TouchControllerConfigScreen(screen))).build(), null);
    }
}