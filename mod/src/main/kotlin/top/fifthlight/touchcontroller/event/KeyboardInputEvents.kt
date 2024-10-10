package top.fifthlight.touchcontroller.event

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.input.KeyboardInput
import top.fifthlight.touchcontroller.event.KeyboardInputEvents.EndInputTick

object KeyboardInputEvents {
    val END_INPUT_TICK: Event<EndInputTick> = EventFactory.createArrayBacked(
        EndInputTick::class.java
    ) { callbacks: Array<EndInputTick> ->
        EndInputTick { input, slowDown, slowDownFactor ->
            for (event in callbacks) {
                event.onEndTick(input, slowDown, slowDownFactor)
            }
        }
    }

    fun interface EndInputTick {
        fun onEndTick(input: KeyboardInput, slowDown: Boolean, slowDownFactor: Float)
    }
}
