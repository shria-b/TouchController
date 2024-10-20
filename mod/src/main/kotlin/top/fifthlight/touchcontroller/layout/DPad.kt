package top.fifthlight.touchcontroller.layout

import net.minecraft.util.Colors
import top.fifthlight.touchcontroller.asset.Textures
import top.fifthlight.touchcontroller.config.control.DPadConfig
import top.fifthlight.touchcontroller.config.control.DPadExtraButton

fun Context.DPad(config: DPadConfig) {
    val buttonSize = config.buttonSize()

    val forward = withRect(
        x = buttonSize.width + config.padding,
        y = 0,
        width = buttonSize.width,
        height = buttonSize.height
    ) {
        SwipeButton(id = "dpad_forward") { clicked ->
            when (Pair(config.classic, clicked)) {
                Pair(true, false) -> Texture(id = Textures.DPAD_UP_CLASSIC)
                Pair(true, true) -> Texture(id = Textures.DPAD_UP_CLASSIC, color = Colors.WHITE)
                Pair(false, false) -> Texture(id = Textures.DPAD_UP)
                Pair(false, true) -> Texture(id = Textures.DPAD_UP_ACTIVE)
            }
        }
    }

    val backward = withRect(
        x = buttonSize.width + config.padding,
        y = (buttonSize.height + config.padding) * 2,
        width = buttonSize.width,
        height = buttonSize.height
    ) {
        SwipeButton(id = "dpad_backward") { clicked ->
            when (Pair(config.classic, clicked)) {
                Pair(true, false) -> Texture(id = Textures.DPAD_DOWN_CLASSIC)
                Pair(true, true) -> Texture(id = Textures.DPAD_DOWN_CLASSIC, color = Colors.WHITE)
                Pair(false, false) -> Texture(id = Textures.DPAD_DOWN)
                Pair(false, true) -> Texture(id = Textures.DPAD_DOWN_ACTIVE)
            }
        }
    }

    val left = withRect(
        x = 0,
        y = buttonSize.height + config.padding,
        width = buttonSize.width,
        height = buttonSize.height
    ) {
        SwipeButton(id = "dpad_left") { clicked ->
            when (Pair(config.classic, clicked)) {
                Pair(true, false) -> Texture(id = Textures.DPAD_LEFT_CLASSIC)
                Pair(true, true) -> Texture(id = Textures.DPAD_LEFT_CLASSIC, color = Colors.WHITE)
                Pair(false, false) -> Texture(id = Textures.DPAD_LEFT)
                Pair(false, true) -> Texture(id = Textures.DPAD_LEFT_ACTIVE)
            }
        }
    }

    val right = withRect(
        x = (buttonSize.width + config.padding) * 2,
        y = buttonSize.height + config.padding,
        width = buttonSize.width,
        height = buttonSize.height
    ) {
        SwipeButton(id = "dpad_right") { clicked ->
            when (Pair(config.classic, clicked)) {
                Pair(true, false) -> Texture(id = Textures.DPAD_RIGHT_CLASSIC)
                Pair(true, true) -> Texture(id = Textures.DPAD_RIGHT_CLASSIC, color = Colors.WHITE)
                Pair(false, false) -> Texture(id = Textures.DPAD_RIGHT)
                Pair(false, true) -> Texture(id = Textures.DPAD_RIGHT_ACTIVE)
            }
        }
    }

    withRect(
        x = buttonSize.width + config.padding,
        y = buttonSize.height + config.padding,
        width = buttonSize.width,
        height = buttonSize.height
    ) {
        when (config.extraButton) {
            DPadExtraButton.NONE -> {}
            DPadExtraButton.SNEAK -> {
                status.sneak = Button(id = "sneak") { clicked ->
                    when (Pair(config.classic, clicked)) {
                        Pair(true, false) -> Texture(id = Textures.SNEAK_CLASSIC)
                        Pair(true, true) -> Texture(id = Textures.SNEAK_CLASSIC, color = Colors.WHITE)
                        Pair(false, false) -> Texture(id = Textures.SNEAK)
                        Pair(false, true) -> Texture(id = Textures.SNEAK_ACTIVE)
                    }
                }
            }

            DPadExtraButton.JUMP -> {
                status.jump = Button(id = "jump") { clicked ->
                    when (Pair(config.classic, clicked)) {
                        Pair(true, false) -> Texture(id = Textures.JUMP_CLASSIC)
                        Pair(true, true) -> Texture(id = Textures.JUMP_CLASSIC, color = Colors.WHITE)
                        Pair(false, false) -> Texture(id = Textures.JUMP)
                        Pair(false, true) -> Texture(id = Textures.JUMP_ACTIVE)
                    }
                }
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