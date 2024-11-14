package top.fifthlight.touchcontroller.layout

import top.fifthlight.touchcontroller.asset.Textures
import top.fifthlight.touchcontroller.control.JumpButton
import top.fifthlight.touchcontroller.proxy.data.IntSize

fun Context.RawJumpButton(classic: Boolean = true, size: IntSize = this.size) {
    if (classic) {
        val (_, clicked) = Button(id = "jump") { clicked ->
            withAlign(align = Align.CENTER_CENTER, size = size) {
                if (state == HudState.NORMAL) Texture(id = Textures.JUMP_CLASSIC) else Texture(id = Textures.JUMP_FLYING)
            }
        }
        result.jump = result.jump || clicked
    } else if (state == HudState.NORMAL) {
        val (_, clicked) = Button(id = "jump") { clicked ->
            withAlign(align = Align.CENTER_CENTER, size = size) {
                if (clicked) Texture(id = Textures.JUMP_ACTIVE) else Texture(id = Textures.JUMP)
            }
        }
        result.jump = result.jump || clicked
    }

}

fun Context.JumpButton(config: JumpButton) {
    RawJumpButton(classic = config.classic)
}