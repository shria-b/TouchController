package top.fifthlight.touchcontroller.layout

import top.fifthlight.touchcontroller.proxy.data.IntSize
import top.fifthlight.touchcontroller.state.ControllerHudConfig
import top.fifthlight.touchcontroller.state.DPadHudLayoutConfig

fun Context.DPad(config: ControllerHudConfig, layout: DPadHudLayoutConfig) {
    val buttonSize = IntSize(width = layout.size, height = layout.size)

    val forward = withOffset(
        x = layout.size + config.padding * 2,
        y = config.padding,
    ) {
        SwipeButton(
            size = buttonSize,
            id = "dpad_forward",
        )
    }

    val backward = withOffset(
        x = layout.size + config.padding * 2,
        y = layout.size * 2 + config.padding * 3,
    ) {
        SwipeButton(
            size = buttonSize,
            id = "dpad_backward",
        )
    }

    val left = withOffset(
        x = config.padding,
        y = layout.size + config.padding * 2,
    ) {
        SwipeButton(
            size = buttonSize,
            id = "dpad_left",
        )
    }

    val right = withOffset(
        x = layout.size * 2 + config.padding * 3,
        y = layout.size + config.padding * 2,
    ) {
        SwipeButton(
            size = buttonSize,
            id = "dpad_right",
        )
    }

    when (Pair(forward, backward)) {
        Pair(true, false) -> status.forward = 1f
        Pair(false, true) -> status.forward = -1f
    }

    when (Pair(left, right)) {
        Pair(true, false) -> status.left = 1f
        Pair(false, true) -> status.left = -1f
    }
}