package top.fifthlight.touchcontroller.proxy.data

data class IntRect(
    val offset: IntOffset,
    val size: IntSize
) {
    val left
        get() = offset.left
    val top
        get() = offset.top
    val right
        get() = offset.left + size.width
    val bottom
        get() = offset.top + size.height
}