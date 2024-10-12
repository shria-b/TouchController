package top.fifthlight.touchcontroller.proxy.data

data class Offset(
    val left: Float,
    val top: Float
) {
    val x
        get() = left
    val y
        get() = top

    companion object {
        val ZERO = Offset(0f, 0f)
    }

    operator fun plus(length: Float) = Offset(left = left + length, top = top + length)
    operator fun minus(length: Float) = Offset(left = left - length, top = top - length)
    operator fun div(num: Float) = Offset(left = left / num, top = top / num)
    operator fun times(num: Float): Offset = Offset(left = left * num, top = top * num)
    operator fun minus(offset: IntOffset) = Offset(left = left - offset.left, top = top - offset.top)
    operator fun minus(offset: Offset) = Offset(left = left - offset.left, top = top - offset.top)
    operator fun times(size: IntSize) = Offset(left = left * size.width.toFloat(), top = top * size.height.toFloat())
}
