package top.fifthlight.touchcontroller.layout

import top.fifthlight.touchcontroller.proxy.data.IntSize
import top.fifthlight.touchcontroller.state.ControllerHudConfig
import top.fifthlight.touchcontroller.state.DPadHudLayoutConfig

fun Context.DPad(config: ControllerHudConfig, layout: DPadHudLayoutConfig) {
    val buttonSize = IntSize(width = layout.size, height = layout.size)

    val forward = withRect(
        x = layout.size + config.padding * 2,
        y = config.padding,
        width = buttonSize.width,
        height = buttonSize.height
    ) {
        SwipeButton(
            id = "dpad_forward",
        )
    }

    val backward = withRect(
        x = layout.size + config.padding * 2,
        y = layout.size * 2 + config.padding * 3,
        width = buttonSize.width,
        height = buttonSize.height
    ) {
        SwipeButton(
            id = "dpad_backward",
        )
    }

    val left = withRect(
        x = config.padding,
        y = layout.size + config.padding * 2,
        width = buttonSize.width,
        height = buttonSize.height
    ) {
        SwipeButton(
            id = "dpad_left",
        )
    }

    val right = withRect(
        x = layout.size * 2 + config.padding * 3,
        y = layout.size + config.padding * 2,
        width = buttonSize.width,
        height = buttonSize.height
    ) {
        SwipeButton(
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