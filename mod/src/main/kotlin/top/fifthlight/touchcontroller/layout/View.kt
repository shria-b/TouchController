package top.fifthlight.touchcontroller.layout

import net.minecraft.util.hit.HitResult.Type.*
import top.fifthlight.touchcontroller.ext.size
import top.fifthlight.touchcontroller.mixin.ClientPlayerInteractionManagerAccessor
import top.fifthlight.touchcontroller.state.PointerState

fun Context.View() {
    var releasedView = false
    for (key in pointers.keys.toList()) {
        val state = pointers[key]!!.state
        if (state is PointerState.Released) {
            if (!releasedView && state.previousState is PointerState.View) {
                releasedView = true
                val previousState = state.previousState
                val pressTime = timer.tick - previousState.pressTime
                if (pressTime < 5 && !previousState.moving) {
                    val crosshairTarget = client.crosshairTarget ?: break
                    when (crosshairTarget.type) {
                        BLOCK -> {
                            result.crosshairStatus = CrosshairStatus(
                                position = state.previousPosition,
                                breakPercent = 0f
                            )
                            status.itemUse.add()
                        }
                        ENTITY -> {
                            result.crosshairStatus = CrosshairStatus(
                                position = state.previousPosition,
                                breakPercent = 0f
                            )
                            status.attack.add()
                        }
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

        var moving = state.moving
        if (!state.moving) {
            val delta = (pointer.rawOffset - state.initialPosition).squaredLength
            val threshold = (client.window.size.toSize() * 0.02f).squaredLength
            if (delta > threshold) {
                moving = true
            }
        }
        result.lookDirection = pointer.rawOffset - state.lastPosition

        if (state.consumed) {
            pointer.state = state.copy(lastPosition = pointer.rawOffset, moving = moving)
            return@let
        }

        var consumed = false
        val pressTime = timer.tick - state.pressTime
        var destroyTriggered = state.destroyTriggered
        val crosshairTarget = client.crosshairTarget
        if (pressTime == 5 && !moving) {
            when (crosshairTarget?.type) {
                BLOCK -> destroyTriggered = true

                ENTITY -> {
                    status.itemUse.add()
                    consumed = true
                }

                MISS, null -> {}
            }
        }
        if (!consumed && destroyTriggered && crosshairTarget?.type == BLOCK) {
            status.attack.add()
        }

        pointer.state = state.copy(
            lastPosition = pointer.rawOffset, moving = moving, destroyTriggered = destroyTriggered, consumed = consumed
        )
    } ?: run {
        pointers.values.forEach {
            when (it.state) {
                PointerState.New -> {
                    if (currentViewPointer != null) {
                        it.state = PointerState.Invalid
                    } else {
                        it.state = PointerState.View(
                            initialPosition = it.rawOffset, lastPosition = it.rawOffset, pressTime = timer.tick
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

        result.crosshairStatus = CrosshairStatus(
            position = pointer.position,
            breakPercent = accessor.currentBreakingProgress,
        )
    }
}