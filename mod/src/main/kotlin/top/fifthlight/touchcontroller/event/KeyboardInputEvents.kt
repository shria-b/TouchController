package top.fifthlight.touchcontroller.event

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.input.KeyboardInput

object KeyboardInputEvents {
    val END_INPUT_TICK: Event<EndInputTick> = EventFactory.createArrayBacked(
        EndInputTick::class.java
    ) { callbacks: Array<EndInputTick> ->
        EndInputTick { input ->
            for (event in callbacks) {
                event.onEndTick(input)
            }
        }
    }

    fun interface EndInputTick {
        fun onEndTick(input: KeyboardInput)
    }
}
