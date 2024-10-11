package top.fifthlight.touchcontroller.state

import top.fifthlight.touchcontroller.proxy.data.Offset

sealed class PointerState {
    data object New: PointerState()
    data class View(val lastPosition: Offset) : PointerState()
    data object Invalid: PointerState()
    data class Button(val id: String): PointerState()
    data class SwipeButton(val id: String): PointerState()
}

data class Pointer(
    var position: Offset,
    var state: PointerState = PointerState.New
)
