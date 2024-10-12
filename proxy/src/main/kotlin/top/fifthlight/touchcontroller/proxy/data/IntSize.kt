package top.fifthlight.touchcontroller.proxy.data

class IntSize(
    val width: Int,
    val height: Int,
) {
    companion object {
        val ZERO = IntSize(0, 0)
    }

    operator fun contains(offset: Offset): Boolean {
        val x = 0 <= offset.x && offset.x < width
        val y = 0 <= offset.y && offset.y < height
        return x && y
    }

    operator fun plus(length: Int) = IntSize(width = width + length, height = height + length)
    operator fun minus(offset: IntOffset) = IntSize(width = width - offset.left, height = height - offset.top)
}