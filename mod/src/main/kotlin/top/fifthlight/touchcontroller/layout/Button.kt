package top.fifthlight.touchcontroller.layout

import top.fifthlight.touchcontroller.state.PointerState

fun Context.SwipeButton(
    id: String,
    content: Context.(clicked: Boolean) -> Unit,
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
    content(clicked)
    return clicked
}

fun Context.Button(
    id: String,
    content: Context.(clicked: Boolean) -> Unit,
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

    content(clicked)
    return clicked
}