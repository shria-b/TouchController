package top.fifthlight.touchcontroller.handler

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderTickCounter
import top.fifthlight.touchcontroller.render.ControllerHudRenderer

class HudCallbackHandler: HudRenderCallback {
    override fun onHudRender(drawContext: DrawContext, tickCounter: RenderTickCounter) {
        ControllerHudRenderer.render(drawContext, tickCounter)
    }
}
