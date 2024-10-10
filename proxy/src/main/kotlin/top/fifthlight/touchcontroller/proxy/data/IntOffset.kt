package top.fifthlight.touchcontroller.proxy.data

@JvmInline
value class IntOffset(
    private val packed: Long,
) {
    val left: Int
        get() = (packed shr 32).toInt()
    val top: Int
        get() = packed.toInt()
    val x
        get() = left
    val y
        get() = top

    companion object {
        val ZERO = IntOffset(0)
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
}

fun IntOffset(left: Int, top: Int) = IntOffset((left.toLong() shl 32) or top.toLong())