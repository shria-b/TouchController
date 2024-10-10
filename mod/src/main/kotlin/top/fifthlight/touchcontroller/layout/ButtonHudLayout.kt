package top.fifthlight.touchcontroller.layout

import top.fifthlight.touchcontroller.proxy.data.IntOffset
import top.fifthlight.touchcontroller.proxy.data.IntSize
import top.fifthlight.touchcontroller.state.ButtonHudLayoutConfig

data class ButtonLayout(
    val offset: IntOffset,
    val size: IntSize
)

class ButtonHudLayout(
    config: ButtonHudLayoutConfig,
    padding: Int
) {
    val size = IntSize(
        width = 3 * config.size + 4 * padding,
        height = 3 * config.size + 4 * padding,
    )

    private val buttonSize = IntSize(config.size, config.size)

    val forward = ButtonLayout(
        offset = IntOffset(
            left = config.size + padding * 2,
            top = padding,
        ),
        size = buttonSize
    )
    val backward = ButtonLayout(
        offset = IntOffset(
            left = config.size + padding * 2,
            top = config.size * 2 + padding * 3,
        ),
        size = buttonSize
    )
    val left = ButtonLayout(
        offset = IntOffset(
            left = padding,
            top = config.size + padding * 2,
        ),
        size = buttonSize
    )
    val right = ButtonLayout(
        offset = IntOffset(
            left = config.size * 2 + padding * 3,
            top = config.size + padding * 2,
        ),
        size = buttonSize
    )
}