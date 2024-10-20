package top.fifthlight.touchcontroller.layout

import top.fifthlight.touchcontroller.asset.Textures
import top.fifthlight.touchcontroller.config.control.JoystickConfig
import top.fifthlight.touchcontroller.ext.drawTexture
import top.fifthlight.touchcontroller.proxy.data.Offset
import top.fifthlight.touchcontroller.proxy.data.Rect
import top.fifthlight.touchcontroller.state.PointerState
import kotlin.math.sqrt

fun Context.Joystick(layout: JoystickConfig) {
    var currentPointer = pointers.values.firstOrNull {
        it.state is PointerState.Joystick
    }
    currentPointer?.let {
        pointers.values.forEach {
            when (it.state) {
                PointerState.New -> it.state = PointerState.Invalid
                else -> {}
            }
        }
    } ?: run {
        for (pointer in pointers.values) {
            when (pointer.state) {
                PointerState.New -> {
                    if (!pointer.inRect(size)) {
                        continue
                    }
                    if (currentPointer != null) {
                        pointer.state = PointerState.Invalid
                    } else {
                        pointer.state = PointerState.Joystick
                        currentPointer = pointer
                    }
                }

                else -> {}
            }
        }
    }

    val normalizedOffset = currentPointer?.let { pointer ->
        val offset = pointer.scaledOffset / size.width.toFloat() * 2f - 1f
        val squaredLength = offset.x * offset.x + offset.y * offset.y
        if (squaredLength > 1) {
            val length = sqrt(squaredLength)
            offset / length
        } else {
            offset
        }
    }

    drawContext.drawTexture(
        id = Textures.JOYSTICK_PAD,
        dstRect = Rect(size = size.toSize())
    )
    val drawOffset = normalizedOffset ?: Offset.ZERO
    val stickSize = layout.stickSize()
    val actualOffset = ((drawOffset + 1f) / 2f * size) - stickSize.toSize() / 2f
    drawContext.drawTexture(
        id = Textures.JOYSTICK_STICK,
        dstRect = Rect(
            offset = actualOffset,
            size = stickSize.toSize()
        )
    )

    normalizedOffset?.let { (right, backward) ->
        result.left = -right
        result.forward = -backward
    }
}