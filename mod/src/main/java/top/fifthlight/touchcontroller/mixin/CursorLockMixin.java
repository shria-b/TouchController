package top.fifthlight.touchcontroller.mixin;

import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.fifthlight.touchcontroller.TouchController;

@Mixin(InputUtil.class)
public abstract class CursorLockMixin {
	@Inject(at = @At("HEAD"), method = "setCursorParameters")
	private static void setCursorParameters(long handler, int inputModeValue, double x, double y, CallbackInfo info) {
		if (inputModeValue == InputUtil.GLFW_CURSOR_DISABLED && TouchController.INSTANCE.getDisableCursorLock()) {
			inputModeValue = InputUtil.GLFW_CURSOR_NORMAL;
		}
	}
}