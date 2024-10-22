package top.fifthlight.touchcontroller.layout

import top.fifthlight.touchcontroller.asset.Textures
import top.fifthlight.touchcontroller.control.JumpButton
import top.fifthlight.touchcontroller.proxy.data.IntSize

fun Context.RawJumpButton(classic: Boolean = true, size: IntSize = this.size) {
    val (_, clicked) = Button(id = "jump") { clicked ->
        withAlign(align = Align.CENTER_CENTER, size = size) {
            when (Pair(classic, clicked)) {
                Pair(true, false) -> Texture(id = Textures.JUMP_CLASSIC)
                Pair(true, true) -> Texture(id = Textures.JUMP_CLASSIC)
                Pair(false, false) -> Texture(id = Textures.JUMP)
                Pair(false, true) -> Texture(id = Textures.JUMP_ACTIVE)
            }
        }
    }

    result.jump = result.jump || clicked
}

fun Context.JumpButton(config: JumpButton) {
    RawJumpButton(classic = config.classic)
}