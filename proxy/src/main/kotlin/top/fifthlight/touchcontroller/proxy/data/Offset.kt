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

    fun inRegion(offset: IntOffset, size: IntSize): Boolean {
        val x = offset.x <= x && x < offset.x + size.width
        val y = offset.y <= y && y < offset.y + size.height
        return x && y
    }

    operator fun plus(length: Float) = Offset(left = left + length, top = top + length)
    operator fun div(num: Float) = Offset(left = left / num, top = top / num)
}
