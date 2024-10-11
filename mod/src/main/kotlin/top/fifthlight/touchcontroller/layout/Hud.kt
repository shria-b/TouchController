package top.fifthlight.touchcontroller.layout

import top.fifthlight.touchcontroller.model.CrosshairStateModel
import top.fifthlight.touchcontroller.proxy.data.IntSize
import top.fifthlight.touchcontroller.proxy.data.Offset
import top.fifthlight.touchcontroller.state.ButtonHudLayoutConfig
import top.fifthlight.touchcontroller.state.ControllerHudConfig
import top.fifthlight.touchcontroller.state.JoystickHudLayoutConfig

fun Context.Hud(config: ControllerHudConfig, crosshairModel: CrosshairStateModel, onViewDelta: (Offset) -> Unit) {
    when (val layout = config.layout) {
        is ButtonHudLayoutConfig -> {
            val size = IntSize(
                width = 3 * layout.size + 4 * config.padding,
                height = 3 * layout.size + 4 * config.padding,
            )
            withAlign(config.align, size) {
                DPad(config, layout)
            }
        }
        is JoystickHudLayoutConfig -> TODO()
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