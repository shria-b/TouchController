package top.fifthlight.touchcontroller.proxy.data

class Size(
    val width: Float,
    val height: Float,
) {
    companion object {
        val ZERO = Size(0f, 0f)
        val ONE = Size(1f, 1f)
    }

    operator fun contains(offset: Offset): Boolean {
        val x = 0 <= offset.x && offset.x < width
        val y = 0 <= offset.y && offset.y < height
        return x && y
    }

    operator fun plus(length: Float) = Size(width = width + length, height = height + length)
    operator fun minus(offset: Offset) = Size(width = width - offset.left, height = height - offset.top)
}