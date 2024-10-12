package top.fifthlight.touchcontroller.proxy.data

data class IntOffset(
    val left: Int,
    val top: Int,
) {
    val x
        get() = left
    val y
        get() = top

    companion object {
        val ZERO = IntOffset(0, 0)
    }

    fun inRegion(offset: IntOffset, size: IntSize): Boolean {
        val x = offset.x <= x && x < offset.x + size.width
        val y = offset.y <= y && y < offset.y + size.height
        return x && y
    }

    operator fun plus(length: Int) = IntOffset(left = left + length, top = top + length)
    operator fun plus(other: IntSize) = IntOffset(
        left = left + other.width,
        top = top + other.height
    )
    operator fun plus(other: IntOffset) = IntOffset(
        left = left + other.left,
        top = top + other.top
    )
    operator fun minus(length: Int) = IntOffset(left = left - length, top = top - length)
    operator fun minus(other: IntSize) = IntOffset(
        left = left - other.width,
        top = top - other.height
    )
    operator fun minus(other: IntOffset) = IntOffset(
        left = left - other.left,
        top = top - other.top
    )
    operator fun times(num: Int) = IntOffset(left = left * num, top = top * num)
    operator fun div(num: Int) = IntOffset(left = left / num, top = top / num)

    override fun toString(): String {
        return "IntOffset(left=$left, top=$top)"
    }
}
