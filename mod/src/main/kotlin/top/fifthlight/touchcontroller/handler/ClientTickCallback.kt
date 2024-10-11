package top.fifthlight.touchcontroller.handler

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.MinecraftClient
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import top.fifthlight.touchcontroller.SocketProxyHolder
import top.fifthlight.touchcontroller.model.GlobalStateModel
import top.fifthlight.touchcontroller.model.TouchStateModel
import top.fifthlight.touchcontroller.proxy.data.Offset
import top.fifthlight.touchcontroller.proxy.message.AddPointerMessage
import top.fifthlight.touchcontroller.proxy.message.VersionMessage
import top.fifthlight.touchcontroller.state.PointerState

class ClientTickStartCallback: ClientTickEvents.StartTick, KoinComponent {
    private val handler: SocketProxyHolder by inject()
    private val touchStateModel: TouchStateModel by inject()
    private val globalStateModel: GlobalStateModel by inject()

    override fun onStartTick(client: MinecraftClient) {
        globalStateModel.update(client)

        handler.socketProxy?.let { proxy ->
            proxy.receive { message ->
                when (message) {
                    is VersionMessage -> {}
                    is AddPointerMessage -> {
                        touchStateModel.addPointer(PointerState(
                            index = message.index,
                            position = message.position
                        ))
                    }
                }
            }
        } ?: run {
            val mouse = client.mouse
            if (mouse.wasLeftButtonClicked()) {
                val mousePosition = Offset(
                    left = client.mouse.x.toFloat(),
                    top = client.mouse.y.toFloat()
                )
                touchStateModel.addPointer(
                    PointerState(
                        index = 0,
                        position = mousePosition
                    )
                )
            } else {
                touchStateModel.clearPointer()
            }
        }
    }
}