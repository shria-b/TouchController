package top.fifthlight.touchcontroller.handler

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents.BeforeBlockOutline
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderTickCounter
import net.minecraft.util.hit.HitResult
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import top.fifthlight.touchcontroller.SocketProxyHolder
import top.fifthlight.touchcontroller.config.TouchControllerConfigHolder
import top.fifthlight.touchcontroller.event.HudRenderCallback
import top.fifthlight.touchcontroller.model.CrosshairStateModel
import top.fifthlight.touchcontroller.model.GlobalStateModel
import top.fifthlight.touchcontroller.model.TouchStateModel
import top.fifthlight.touchcontroller.proxy.data.Offset
import top.fifthlight.touchcontroller.proxy.message.AddPointerMessage
import top.fifthlight.touchcontroller.proxy.message.VersionMessage

class WorldRendererHandler: WorldRenderEvents.Start, BeforeBlockOutline, HudRenderCallback.CrosshairRender, KoinComponent {
    private val handler: SocketProxyHolder by inject()
    private val touchStateModel: TouchStateModel by inject()
    private val globalStateModel: GlobalStateModel by inject()
    private val crosshairStateModel: CrosshairStateModel by inject()
    private val configHolder: TouchControllerConfigHolder by inject()
    private val client: MinecraftClient by inject()

    override fun beforeBlockOutline(context: WorldRenderContext, hitResult: HitResult?): Boolean {
        return crosshairStateModel.state.status != null
    }

    override fun onCrosshairRender(drawContext: DrawContext, tickCounter: RenderTickCounter): Boolean {
        return false
    }

    override fun onStart(context: WorldRenderContext) {
        globalStateModel.update(client)

        handler.socketProxy?.let { proxy ->
            proxy.receive { message ->
                when (message) {
                    is VersionMessage -> {}
                    is AddPointerMessage -> {
                        touchStateModel.addPointer(
                            index = message.index,
                            position = message.position
                        )
                    }
                }
            }
        }

        val config = configHolder.config.value
        if (config.enableTouchEmulation) {
            val mouse = client.mouse
            if (mouse.wasLeftButtonClicked()) {
                val mousePosition = Offset(
                    left = (mouse.x / client.window.width).toFloat(),
                    top = (mouse.y / client.window.height).toFloat()
                )
                touchStateModel.addPointer(
                    index = 0,
                    position = mousePosition
                )
            } else {
                touchStateModel.clearPointer()
            }
        }
    }
}
