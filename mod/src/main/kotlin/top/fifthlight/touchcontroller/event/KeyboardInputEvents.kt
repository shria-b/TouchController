package top.fifthlight.touchcontroller.event

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.input.KeyboardInput

object KeyboardInputEvents {
    // 将END_INPUT_TICK声明为public以允许在其他包中访问
    public val END_INPUT_TICK: Event<EndInputTick> = EventFactory.createArrayBacked(
        EndInputTick::class.java
    ) { callbacks: Array<EndInputTick> ->
        EndInputTick { input ->
            for (event in callbacks) {
                event.onEndTick(input)
            }
        }
    }

    public fun interface EndInputTick {
        fun onEndTick(input: KeyboardInput)
    }
}
