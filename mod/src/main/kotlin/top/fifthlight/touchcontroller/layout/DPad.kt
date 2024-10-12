package top.fifthlight.touchcontroller.layout

import top.fifthlight.touchcontroller.proxy.data.IntSize
import top.fifthlight.touchcontroller.state.DPadHudLayoutConfig
import top.fifthlight.touchcontroller.state.ControllerHudConfig

fun Context.DPad(config: ControllerHudConfig, layout: DPadHudLayoutConfig) {
    val buttonSize = IntSize(width = layout.size, height = layout.size)

    status.forward = withOffset(
        x = layout.size + config.padding * 2,
        y = config.padding,
    ) {
        SwipeButton(
            size = buttonSize,
            id = "dpad_forward",
        )
    }

    status.backward = withOffset(
        x = layout.size + config.padding * 2,
        y = layout.size * 2 + config.padding * 3,
    ) {
        SwipeButton(
            size = buttonSize,
            id = "dpad_backward",
        )
    }

    status.left = withOffset(
        x = config.padding,
        y = layout.size + config.padding * 2,
    ) {
        SwipeButton(
            size = buttonSize,
            id = "dpad_left",
        )
    }

    status.right = withOffset(
        x = layout.size * 2 + config.padding * 3,
        y = layout.size + config.padding * 2,
    ) {
        SwipeButton(
            size = buttonSize,
            id = "dpad_right",
        )
    }
}