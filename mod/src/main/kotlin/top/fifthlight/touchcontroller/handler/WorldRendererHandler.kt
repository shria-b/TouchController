package top.fifthlight.touchcontroller.handler

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents.BeforeBlockOutline
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderTickCounter
import net.minecraft.util.hit.HitResult
import top.fifthlight.touchcontroller.event.HudRenderCallback

class WorldRendererHandler: BeforeBlockOutline, HudRenderCallback.CrosshairRender {
    override fun beforeBlockOutline(context: WorldRenderContext, hitResult: HitResult?): Boolean {
        return false
    }

    override fun onCrosshairRender(drawContext: DrawContext, tickCounter: RenderTickCounter): Boolean {
        return false
    }
}
