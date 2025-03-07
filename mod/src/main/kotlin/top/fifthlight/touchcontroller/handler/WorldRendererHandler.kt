package top.fifthlight.touchcontroller.handler

import kotlinx.coroutines.runBlocking
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents.BeforeBlockOutline
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderTickCounter
import net.minecraft.item.Item
import net.minecraft.item.ProjectileItem
import net.minecraft.item.RangedWeaponItem
import net.minecraft.util.Hand
import net.minecraft.util.hit.HitResult
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import top.fifthlight.touchcontroller.SocketProxyHolder
import top.fifthlight.touchcontroller.config.TouchControllerConfig
import top.fifthlight.touchcontroller.config.TouchControllerConfigHolder
import top.fifthlight.touchcontroller.event.HudRenderCallback
import top.fifthlight.touchcontroller.model.ControllerHudModel
import top.fifthlight.touchcontroller.model.GlobalStateModel
import top.fifthlight.touchcontroller.model.TouchStateModel
import top.fifthlight.touchcontroller.proxy.data.Offset
import top.fifthlight.touchcontroller.proxy.message.AddPointerMessage
import top.fifthlight.touchcontroller.proxy.message.ClearPointerMessage
import top.fifthlight.touchcontroller.proxy.message.RemovePointerMessage

private fun Item.shouldShowCrosshair(config: TouchControllerConfig): Boolean {
    if (config.projectileShowCrosshair && this is ProjectileItem) {
        return true
    } else if (config.rangedWeaponShowCrosshair && this is RangedWeaponItem) {
        return true
    }
    return false
}

class WorldRendererHandler : WorldRenderEvents.Start, BeforeBlockOutline, HudRenderCallback.CrosshairRender,
    KoinComponent {
    private val handler: SocketProxyHolder by inject()
    private val touchStateModel: TouchStateModel by inject()
    private val globalStateModel: GlobalStateModel by inject()
    private val controllerHudModel: ControllerHudModel by inject()
    private val configHolder: TouchControllerConfigHolder by inject()
    private val client: MinecraftClient by inject()

    override fun beforeBlockOutline(context: WorldRenderContext, hitResult: HitResult?): Boolean =
        controllerHudModel.result.crosshairStatus != null

    override fun onCrosshairRender(drawContext: DrawContext, tickCounter: RenderTickCounter): Boolean {
        val config = configHolder.config.value
        if (!config.disableCrosshair) {
            return true
        }
        return client.player?.let { player ->
            for (hand in Hand.entries) {
                val stack = player.getStackInHand(hand)
                if (stack.item.shouldShowCrosshair(config)) {
                    return@let true
                }
            }
            false
        } ?: false
    }

    override fun onStart(context: WorldRenderContext) {
        globalStateModel.update(client)

        handler.socketProxy?.let { proxy ->
            runBlocking {
                proxy.receive { message ->
                    when (message) {
                        is AddPointerMessage -> {
                            touchStateModel.addPointer(
                                index = message.index,
                                position = message.position
                            )
                        }

                        is RemovePointerMessage -> {
                            touchStateModel.removePointer(message.index)
                        }

                        ClearPointerMessage -> touchStateModel.clearPointer()
                    }
                }
            }
        }

        val config = configHolder.config.value
        if (config.enableTouchEmulation) {
            val mouse = client.mouse
            if (mouse.wasLeftButtonClicked()) {
                val mousePosition = Offset(
                    x = (mouse.x / client.window.width).toFloat(),
                    y = (mouse.y / client.window.height).toFloat()
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
