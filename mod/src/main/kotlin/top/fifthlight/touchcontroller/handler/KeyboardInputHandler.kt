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

        val config = controllerHudModel.config.value
        val status = controllerHudModel.status.value

        when (config.layout) {
            is ButtonHudLayoutConfig -> {
                val buttonStatus = status.button
                if (buttonStatus.forward) {
                    input.pressingForward = true
                }
                if (buttonStatus.backward) {
                    input.pressingBack = true
                }
                if (buttonStatus.left) {
                    input.pressingLeft = true
                }
                if (buttonStatus.right) {
                    input.pressingRight = true
                }
            }
            is JoystickHudLayoutConfig -> {

            }
        }

        fun getMovementMultiplier(positive: Boolean, negative: Boolean): Float {
            if (positive == negative) {
                return 0.0f
            }
            return if (positive) 1.0f else -1.0f
        }

        val options = client.options
        input.movementForward = getMovementMultiplier(input.pressingForward, input.pressingBack)
        input.movementSideways = getMovementMultiplier(input.pressingLeft, input.pressingRight)
        input.jumping = options.jumpKey.isPressed
        input.sneaking = options.sneakKey.isPressed
        if (slowDown) {
            input.movementSideways *= slowDownFactor
            input.movementForward *= slowDownFactor
        }
    }
}