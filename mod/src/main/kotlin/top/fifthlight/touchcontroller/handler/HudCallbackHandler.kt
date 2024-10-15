package top.fifthlight.touchcontroller.handler

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderTickCounter
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import top.fifthlight.touchcontroller.config.TouchControllerConfigHolder
import top.fifthlight.touchcontroller.ext.scaledWindowSize
import top.fifthlight.touchcontroller.layout.Context
import top.fifthlight.touchcontroller.layout.Hud
import top.fifthlight.touchcontroller.model.ControllerHudModel
import top.fifthlight.touchcontroller.model.CrosshairStateModel
import top.fifthlight.touchcontroller.model.TouchStateModel
import top.fifthlight.touchcontroller.proxy.data.IntOffset

class HudCallbackHandler : HudRenderCallback, KoinComponent {
    private val configHolder: TouchControllerConfigHolder by inject()
    private val controllerHudModel: ControllerHudModel by inject()
    private val touchStateModel: TouchStateModel by inject()
    private val crosshairStateModel: CrosshairStateModel by inject()
    private val client: MinecraftClient by inject()

    override fun onHudRender(drawContext: DrawContext, tickCounter: RenderTickCounter) {
        val status = Context(
            drawContext = drawContext,
            size = drawContext.scaledWindowSize,
            screenOffset = IntOffset.ZERO,
            scale = client.window.scaleFactor.toFloat(),
            pointers = touchStateModel.pointers
        ).run {
            Hud(
                widgets = configHolder.config.value.widgetConfigs,
                crosshairModel = crosshairStateModel,
                onViewDelta = { (x, y) ->
                    client.player?.changeLookDirection(x.toDouble(), y.toDouble())
                }
            )
            status
        }
        controllerHudModel.state = status
    }
}
