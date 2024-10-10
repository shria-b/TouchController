package top.fifthlight.touchcontroller.proxy.data

@JvmInline
value class IntSize(
    private val packed: Long,
) {
    val width: Int
        get() = (packed shr 32).toInt()
    val height: Int
        get() = packed.toInt()

    operator fun plus(length: Int) = IntSize(width = width + length, height = height + length)
}

fun IntSize(width: Int, height: Int) = IntSize((width.toLong() shl 32) or height.toLong())