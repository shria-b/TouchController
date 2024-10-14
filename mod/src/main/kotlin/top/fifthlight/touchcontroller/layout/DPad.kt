package top.fifthlight.touchcontroller.layout

import net.minecraft.util.Colors
import top.fifthlight.touchcontroller.asset.Textures
import top.fifthlight.touchcontroller.proxy.data.IntSize
import top.fifthlight.touchcontroller.state.DPadHudLayoutConfig

fun Context.DPad(layout: DPadHudLayoutConfig) {
    val buttonSize = IntSize(width = layout.size, height = layout.size)

    val forward = withRect(
        x = layout.size + layout.padding * 2,
        y = layout.padding,
        width = buttonSize.width,
        height = buttonSize.height
    ) {
        SwipeButton(id = "dpad_forward") { clicked ->
            when (Pair(layout.classic, clicked)) {
                Pair(true, false) -> Texture(id = Textures.DPAD_UP_CLASSIC)
                Pair(true, true) -> Texture(id = Textures.DPAD_UP_CLASSIC, color = Colors.WHITE)
                Pair(false, false) -> Texture(id = Textures.DPAD_UP)
                Pair(false, true) -> Texture(id = Textures.DPAD_UP_ACTIVE)
            }
        }
    }

    val backward = withRect(
        x = layout.size + layout.padding * 2,
        y = layout.size * 2 + layout.padding * 3,
        width = buttonSize.width,
        height = buttonSize.height
    ) {
        SwipeButton(id = "dpad_backward") { clicked ->
            when (Pair(layout.classic, clicked)) {
                Pair(true, false) -> Texture(id = Textures.DPAD_DOWN_CLASSIC)
                Pair(true, true) -> Texture(id = Textures.DPAD_DOWN_CLASSIC, color = Colors.WHITE)
                Pair(false, false) -> Texture(id = Textures.DPAD_DOWN)
                Pair(false, true) -> Texture(id = Textures.DPAD_DOWN_ACTIVE)
            }
        }
    }

    val left = withRect(
        x = layout.padding,
        y = layout.size + layout.padding * 2,
        width = buttonSize.width,
        height = buttonSize.height
    ) {
        SwipeButton(id = "dpad_left") { clicked ->
            when (Pair(layout.classic, clicked)) {
                Pair(true, false) -> Texture(id = Textures.DPAD_LEFT_CLASSIC)
                Pair(true, true) -> Texture(id = Textures.DPAD_LEFT_CLASSIC, color = Colors.WHITE)
                Pair(false, false) -> Texture(id = Textures.DPAD_LEFT)
                Pair(false, true) -> Texture(id = Textures.DPAD_LEFT_ACTIVE)
            }
        }
    }

    val right = withRect(
        x = layout.size * 2 + layout.padding * 3,
        y = layout.size + layout.padding * 2,
        width = buttonSize.width,
        height = buttonSize.height
    ) {
        SwipeButton(id = "dpad_right") { clicked ->
            when (Pair(layout.classic, clicked)) {
                Pair(true, false) -> Texture(id = Textures.DPAD_RIGHT_CLASSIC)
                Pair(true, true) -> Texture(id = Textures.DPAD_RIGHT_CLASSIC, color = Colors.WHITE)
                Pair(false, false) -> Texture(id = Textures.DPAD_RIGHT)
                Pair(false, true) -> Texture(id = Textures.DPAD_RIGHT_ACTIVE)
            }
        }
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