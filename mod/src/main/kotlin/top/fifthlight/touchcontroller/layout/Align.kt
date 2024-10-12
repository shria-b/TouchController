package top.fifthlight.touchcontroller.layout

import top.fifthlight.touchcontroller.proxy.data.IntOffset
import top.fifthlight.touchcontroller.proxy.data.IntSize

enum class Align {
    LEFT_TOP,
    RIGHT_TOP,
    LEFT_BOTTOM,
    RIGHT_BOTTOM;

    fun alignOffset(windowSize: IntSize, size: IntSize): IntOffset {
        return when (this) {
            LEFT_TOP -> IntOffset(
                left = 0,
                top = 0,
            )

            RIGHT_TOP -> IntOffset(
                left = windowSize.width - size.width,
                top = 0,
            )

            LEFT_BOTTOM -> IntOffset(
                left = 0,
                top = windowSize.height - size.height,
            )

            RIGHT_BOTTOM -> IntOffset(
                left = windowSize.width - size.width,
                top = windowSize.height - size.height,
            )
        }
    }
}

inline fun <reified T> Context.withAlign(align: Align, size: IntSize, crossinline block: Context.() -> T): T =
    withRect(
        offset = align.alignOffset(windowSize = this.size, size = size),
        size = size,
        block = block
    )