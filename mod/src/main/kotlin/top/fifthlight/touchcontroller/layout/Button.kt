package top.fifthlight.touchcontroller.layout

import net.minecraft.util.Colors
import top.fifthlight.touchcontroller.proxy.data.IntSize
import top.fifthlight.touchcontroller.state.PointerState

private fun Context.BaseButton(
    size: IntSize,
    clicked: Boolean
) {
    val color = if (clicked) {
        Colors.RED
    } else {
        Colors.BLACK
    }
    drawContext.fill(0, 0, size.width, size.height, color)
}

fun Context.SwipeButton(
    size: IntSize,
    id: String,
): Boolean {
    val pointers = getPointersInRect(size)
    var clicked = false
    for (pointer in pointers) {
        when (pointer.state) {
            PointerState.New -> {
                pointer.state = PointerState.SwipeButton(id)
                clicked = true
            }
            is PointerState.SwipeButton -> {
                clicked = true
            }
            else -> {}
        }
    }
    BaseButton(size, clicked)
    return clicked
}


fun Context.Button(
    size: IntSize,
    id: String
): Boolean {
    val pointers = getPointersInRect(size)
    var clicked = false
    for (pointer in pointers) {
        when (val state = pointer.state) {
            PointerState.New -> {
                pointer.state = PointerState.Button(id)
                clicked = true
            }
            is PointerState.Button -> {
                if (state.id == id) {
                    clicked = true
                }
            }
            else -> {}
        }
    }

    BaseButton(size, clicked)
    return clicked
}