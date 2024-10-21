package top.fifthlight.touchcontroller.layout

import top.fifthlight.touchcontroller.asset.Textures
import top.fifthlight.touchcontroller.config.control.JumpButtonConfig

fun Context.RawJumpButton(classic: Boolean = true) {
    val (_, clicked) = Button(id = "jump") { clicked ->
        when (Pair(classic, clicked)) {
            Pair(true, false) -> Texture(id = Textures.JUMP_CLASSIC)
            Pair(true, true) -> Texture(id = Textures.JUMP_CLASSIC)
            Pair(false, false) -> Texture(id = Textures.JUMP)
            Pair(false, true) -> Texture(id = Textures.JUMP_ACTIVE)
        }
    }

    result.jump = result.jump || clicked
}

fun Context.JumpButton(config: JumpButtonConfig) {
    RawJumpButton(classic = config.classic)
}