package top.fifthlight.touchcontroller.layout

import net.minecraft.util.Colors
import top.fifthlight.touchcontroller.state.PointerState

private fun Context.BaseButton(
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
    BaseButton(clicked)
    return clicked
}


fun Context.Button(
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

    BaseButton(clicked)
    return clicked
}