package top.fifthlight.touchcontroller.layout

import net.minecraft.util.Colors
import top.fifthlight.touchcontroller.proxy.data.IntOffset
import top.fifthlight.touchcontroller.proxy.data.IntSize
import top.fifthlight.touchcontroller.proxy.data.Offset
import top.fifthlight.touchcontroller.state.ControllerHudConfig
import top.fifthlight.touchcontroller.state.JoystickHudLayoutConfig
import top.fifthlight.touchcontroller.state.PointerState
import kotlin.math.sqrt

fun Context.Joystick(config: ControllerHudConfig, layout: JoystickHudLayoutConfig) {
    withOffset(config.padding, config.padding) {
        val size = IntSize(layout.size, layout.size)
        var currentPointer = pointers.values.firstOrNull {
            it.state is PointerState.Joystick
        }
        currentPointer?.let { pointer ->
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
            val pointerOffset = pointer.scaledOffset
            val offset = pointerOffset / layout.size.toFloat() * 2f - 1f
            val squaredLength = offset.x * offset.x + offset.y * offset.y
            if (squaredLength > 1) {
                val length = sqrt(squaredLength)
                offset / length
            } else {
                offset
            }
        }

        drawContext.fill(0, 0, layout.size, layout.size, Colors.BLACK)
        val drawOffset = normalizedOffset ?: Offset(0f, 0f)
        val actualOffset = ((drawOffset + 1f) / 2f * layout.size.toFloat()).toIntOffset() - IntOffset(
            layout.stickSize,
            layout.stickSize
        ) / 2
        drawContext.fill(
            actualOffset.x,
            actualOffset.y,
            actualOffset.x + layout.stickSize,
            actualOffset.y + layout.stickSize,
            Colors.RED
        )

        normalizedOffset?.let { (right, backward) ->
            status.left = -right
            status.forward = -backward
        }
    }
}