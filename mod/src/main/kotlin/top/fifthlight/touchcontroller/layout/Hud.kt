package top.fifthlight.touchcontroller.layout

import top.fifthlight.touchcontroller.model.CrosshairStateModel
import top.fifthlight.touchcontroller.proxy.data.IntSize
import top.fifthlight.touchcontroller.proxy.data.Offset
import top.fifthlight.touchcontroller.state.ControllerHudConfig
import top.fifthlight.touchcontroller.state.DPadHudLayoutConfig
import top.fifthlight.touchcontroller.state.JoystickHudLayoutConfig

fun Context.Hud(config: ControllerHudConfig, crosshairModel: CrosshairStateModel, onViewDelta: (Offset) -> Unit) {
    when (val layout = config.layout) {
        is DPadHudLayoutConfig -> {
            val size = IntSize(
                width = 3 * layout.size + 4 * layout.padding,
                height = 3 * layout.size + 4 * layout.padding,
            )

            withAlign(layout.align, size) {
                DPad(layout)
            }
        }

        is JoystickHudLayoutConfig -> {
            val size = IntSize(
                width = layout.size + 2 * layout.padding,
                height = layout.size + 2 * layout.padding
            )
            withAlign(layout.align, size) {
                Joystick(layout)
            }
        }
    }

    View(
        crosshairStatus = crosshairModel.state.status,
        onNewCrosshairStatus = {
            crosshairModel.updateStatus(it)
        },
        onPointerDelta = onViewDelta
    )
    Crosshair(crosshairModel.state)
}