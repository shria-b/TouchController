package top.fifthlight.touchcontroller.handler

import net.minecraft.client.MinecraftClient
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import top.fifthlight.touchcontroller.config.TouchControllerConfigHolder
import top.fifthlight.touchcontroller.event.ClientRenderEvents
import top.fifthlight.touchcontroller.ext.scaledSize
import top.fifthlight.touchcontroller.layout.Context
import top.fifthlight.touchcontroller.layout.DrawQueue
import top.fifthlight.touchcontroller.layout.Hud
import top.fifthlight.touchcontroller.mixin.ClientOpenChatScreenInvoker
import top.fifthlight.touchcontroller.model.ControllerHudModel
import top.fifthlight.touchcontroller.model.TouchStateModel
import top.fifthlight.touchcontroller.proxy.data.IntOffset

class ClientRenderHandler : ClientRenderEvents.StartRenderTick, KoinComponent {
    private val configHolder: TouchControllerConfigHolder by inject()
    private val controllerHudModel: ControllerHudModel by inject()
    private val touchStateModel: TouchStateModel by inject()

    override fun onStartTick(client: MinecraftClient, tick: Boolean) {
        val config = configHolder.config.value
        val layout = configHolder.layout.value

        val drawQueue = DrawQueue()
        val result = Context(
            drawQueue = drawQueue,
            size = client.window.scaledSize,
            screenOffset = IntOffset.ZERO,
            scale = client.window.scaleFactor.toFloat(),
            pointers = touchStateModel.pointers,
            status = controllerHudModel.status,
            timer = controllerHudModel.timer
        ).run {
            Hud(
                widgets = layout,
                crosshairConfig = config.crosshair
            )
            result
        }
        controllerHudModel.result = result
        controllerHudModel.pendingDrawQueue = drawQueue

        if (result.chat) {
            (client as ClientOpenChatScreenInvoker).callOpenChatScreen("")
        }
        if (result.pause) {
            client.openGameMenu(false)
        }
        result.lookDirection?.let { (x, y) ->
            client.player?.changeLookDirection(x.toDouble(), y.toDouble())
        }
    }
}