package top.fifthlight.touchcontroller.handler

import net.minecraft.client.MinecraftClient
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import top.fifthlight.touchcontroller.event.ClientHandleInputEvents
import top.fifthlight.touchcontroller.model.ControllerHudModel

class ClientInputHandler: ClientHandleInputEvents.HandleInput, KoinComponent {
    private val controllerHudModel: ControllerHudModel by inject()

    override fun onHandleInput(client: MinecraftClient, context: ClientHandleInputEvents.InputContext) {
        val status = controllerHudModel.status
        while (status.attack.consume()) {
            if (context.doAttack()) {
                context.hitEmptyBlockState()
            }
        }
        while (status.itemUse.consume()) {
            context.doItemUse()
        }
    }
}