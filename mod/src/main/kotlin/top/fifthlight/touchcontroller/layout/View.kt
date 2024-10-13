package top.fifthlight.touchcontroller.layout

import top.fifthlight.touchcontroller.mixin.ClientPlayerInteractionManagerAccessor
import top.fifthlight.touchcontroller.proxy.data.Offset
import top.fifthlight.touchcontroller.state.CrosshairStatus
import top.fifthlight.touchcontroller.state.PointerState

fun Context.View(crosshairStatus: CrosshairStatus?, onNewCrosshairStatus: (CrosshairStatus?) -> Unit, onPointerDelta: (Offset) -> Unit) {
    var currentViewPointer = pointers.values.firstOrNull {
        it.state is PointerState.View
    }

    currentViewPointer?.let { pointer ->
        val state = pointer.state as PointerState.View
        pointers.values.forEach {
            when (it.state) {
                PointerState.New -> it.state = PointerState.Invalid
                else -> {}
            }
        }
        onPointerDelta(pointer.position - state.lastPosition)
        pointer.state = PointerState.View(pointer.position)
    } ?: run {
        pointers.values.forEach {
            when (it.state) {
                PointerState.New -> {
                    if (currentViewPointer != null) {
                        it.state = PointerState.Invalid
                    } else {
                        it.state = PointerState.View(it.position)
                        currentViewPointer = it
                    }
                }
                else -> {}
            }
        }
    }

    currentViewPointer?.let { pointer ->
        val manager = client.interactionManager
        val accessor = manager as ClientPlayerInteractionManagerAccessor
        onNewCrosshairStatus(CrosshairStatus(
            position = pointer.position,
            breakPercent = accessor.currentBreakingProgress,
        ))
    } ?: run {
        if (crosshairStatus != null) {
            onNewCrosshairStatus(null)
        }
    }
}