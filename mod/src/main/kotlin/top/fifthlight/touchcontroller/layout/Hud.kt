package top.fifthlight.touchcontroller.layout

import top.fifthlight.touchcontroller.proxy.data.IntSize
import top.fifthlight.touchcontroller.state.ButtonHudLayoutConfig
import top.fifthlight.touchcontroller.state.ControllerHudConfig
import top.fifthlight.touchcontroller.state.CrosshairState
import top.fifthlight.touchcontroller.state.JoystickHudLayoutConfig

fun Context.Hud(config: ControllerHudConfig, crosshairState: CrosshairState) {
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

    Crosshair(crosshairState)
}