package top.fifthlight.touchcontroller.proxy.data

data class Rect(
    val offset: Offset = Offset.ZERO,
    val size: Size
) {
    companion object {
        val ONE = Rect(
            offset = Offset.ZERO,
            size = Size.ONE
        )
    }

    val left
        get() = offset.left
    val top
        get() = offset.top
    val right
        get() = offset.left + size.width
    val bottom
        get() = offset.top + size.height
}