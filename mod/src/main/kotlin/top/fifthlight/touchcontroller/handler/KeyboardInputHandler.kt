package top.fifthlight.touchcontroller.handler

import net.minecraft.client.MinecraftClient
import net.minecraft.client.input.KeyboardInput
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import top.fifthlight.touchcontroller.event.KeyboardInputEvents
import top.fifthlight.touchcontroller.model.ControllerHudModel
import top.fifthlight.touchcontroller.state.ButtonHudLayoutConfig
import top.fifthlight.touchcontroller.state.JoystickHudLayoutConfig

class KeyboardInputHandler: KeyboardInputEvents.EndInputTick, KoinComponent {
    private val controllerHudModel: ControllerHudModel by inject()

    override fun onEndTick(input: KeyboardInput, slowDown: Boolean, slowDownFactor: Float) {
        val client = MinecraftClient.getInstance()
        if (client.currentScreen != null) {
            return
        }

        val config = controllerHudModel.config
        val state = controllerHudModel.state

        when (config.layout) {
            is ButtonHudLayoutConfig -> {
                if (state.forward) {
                    input.pressingForward = true
                }
                if (state.backward) {
                    input.pressingBack = true
                }
                if (state.left) {
                    input.pressingLeft = true
                }
                if (state.right) {
                    input.pressingRight = true
                }
            }
            is JoystickHudLayoutConfig -> {

            }
        }
    }
}