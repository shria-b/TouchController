package top.fifthlight.touchcontroller.handler

import net.minecraft.client.MinecraftClient
import net.minecraft.client.input.KeyboardInput
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import top.fifthlight.touchcontroller.event.KeyboardInputEvents
import top.fifthlight.touchcontroller.model.ControllerHudModel

class KeyboardInputHandler: KeyboardInputEvents.EndInputTick, KoinComponent {
    private val controllerHudModel: ControllerHudModel by inject()
    private val client: MinecraftClient by inject()

    override fun onEndTick(input: KeyboardInput) {
        if (client.currentScreen != null) {
            return
        }

        val state = controllerHudModel.state

        input.movementForward += state.forward
        input.movementSideways += state.left
        input.movementForward = input.movementForward.coerceIn(-1f, 1f)
        input.movementSideways = input.movementSideways.coerceIn(-1f, 1f)
        input.sneaking = input.sneaking || state.sneak
        input.jumping = input.jumping || state.jump
    }
}