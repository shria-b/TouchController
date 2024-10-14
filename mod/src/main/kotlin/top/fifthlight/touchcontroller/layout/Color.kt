package top.fifthlight.touchcontroller.layout

fun Context.Color(color: Int) {
    drawContext.fill(0, 0, size.width, size.height, color)
}