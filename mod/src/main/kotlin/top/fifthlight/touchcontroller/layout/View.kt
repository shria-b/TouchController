package top.fifthlight.touchcontroller.layout

import net.minecraft.util.hit.HitResult.Type.*
import top.fifthlight.touchcontroller.ext.size
import top.fifthlight.touchcontroller.mixin.ClientPlayerInteractionManagerAccessor
import top.fifthlight.touchcontroller.proxy.data.Offset
import top.fifthlight.touchcontroller.proxy.data.div
import top.fifthlight.touchcontroller.state.CrosshairStatus
import top.fifthlight.touchcontroller.state.PointerState

fun Context.View(
    crosshairStatus: CrosshairStatus?,
    onNewCrosshairStatus: (CrosshairStatus?) -> Unit,
    onPointerDelta: (Offset) -> Unit
) {
    for (key in pointers.keys.toList()) {
        val state = pointers[key]!!.state
        if (state is PointerState.Released) {
            if (state.previousState is PointerState.View) {
                val previousState = state.previousState
                val pressTime = timer.tick - previousState.pressTime
                if (pressTime < 10 && !previousState.moveTriggered) {
                    val crosshairTarget = client.crosshairTarget ?: break
                    when (crosshairTarget.type) {
                        BLOCK -> result.itemUse.add()
                        ENTITY -> result.attack.add()
                        MISS, null -> {}
                    }
                }
            }
            pointers.remove(key)
        }
    }

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

        var moveTriggered = state.moveTriggered
        if (!state.moveTriggered) {
            val delta = (pointer.rawOffset - state.initialPosition).squaredLength
            val threshold = (8f / client.window.size.toSize()).squaredLength
            if (delta > threshold) {
                moveTriggered = true
            }
        }

        if (state.consumed) {
            pointer.state = state.copy(lastPosition = pointer.rawOffset, moveTriggered = moveTriggered)
            return@let
        }

        var consumed = false
        val pressTime = timer.tick - state.pressTime
        if (pressTime >= 10) {
            val crosshairTarget = client.crosshairTarget
            when (crosshairTarget?.type) {
                BLOCK -> result.attack.add()
                ENTITY -> {
                    result.itemUse.add()
                    consumed = true
                }

                MISS, null -> {}
            }
        }

        if (moveTriggered) {
            onPointerDelta(pointer.rawOffset - state.lastPosition)
        }
        pointer.state = state.copy(lastPosition = pointer.rawOffset, moveTriggered = moveTriggered, consumed = consumed)
    } ?: run {
        pointers.values.forEach {
            when (it.state) {
                PointerState.New -> {
                    if (currentViewPointer != null) {
                        it.state = PointerState.Invalid
                    } else {
                        it.state = PointerState.View(
                            initialPosition = it.rawOffset,
                            lastPosition = it.rawOffset,
                            pressTime = timer.tick
                        )
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
        onNewCrosshairStatus(
            CrosshairStatus(
                position = pointer.position,
                breakPercent = accessor.currentBreakingProgress,
            )
        )
    } ?: run {
        if (crosshairStatus != null) {
            onNewCrosshairStatus(null)
        }
    }
}