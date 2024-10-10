package top.fifthlight.touchcontroller.state

import top.fifthlight.touchcontroller.proxy.data.Offset

data class PointerState(
    val index: Int,
    val position: Offset
)

data class TouchState(
    val pointers: List<PointerState> = listOf()
)
