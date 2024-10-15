package top.fifthlight.touchcontroller.layout

import top.fifthlight.touchcontroller.proxy.data.IntOffset
import top.fifthlight.touchcontroller.proxy.data.IntSize

enum class Align {
    LEFT_TOP,
    RIGHT_TOP,
    LEFT_BOTTOM,
    RIGHT_BOTTOM;

    fun alignOffset(windowSize: IntSize, size: IntSize, offset: IntOffset): IntOffset {
        return when (this) {
            LEFT_TOP -> offset

            RIGHT_TOP -> IntOffset(
                x = windowSize.width - size.width - offset.x,
                y = offset.y,
            )

            LEFT_BOTTOM -> IntOffset(
                x = offset.x,
                y = windowSize.height - size.height - offset.y,
            )

            RIGHT_BOTTOM -> IntOffset(
                x = windowSize.width - size.width - offset.x,
                y = windowSize.height - size.height - offset.y,
            )
        }
    }
}

inline fun <reified T> Context.withAlign(align: Align, size: IntSize, offset: IntOffset = IntOffset.ZERO, crossinline block: Context.() -> T): T =
    withRect(
        offset = align.alignOffset(windowSize = this.size, offset = offset, size = size),
        size = size,
        block = block
    )