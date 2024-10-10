package top.fifthlight.touchcontroller.render

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderTickCounter
import net.minecraft.util.Colors
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import top.fifthlight.touchcontroller.ext.scaledWindowSize
import top.fifthlight.touchcontroller.layout.ButtonHudLayout
import top.fifthlight.touchcontroller.layout.ButtonLayout
import top.fifthlight.touchcontroller.model.ControllerHudModel
import top.fifthlight.touchcontroller.state.*

object ControllerHudRenderer : KoinComponent {
    private val viewModel: ControllerHudModel by inject()

    private fun drawButton(
        drawContext: DrawContext,
        config: ControllerHudConfig,
        status: ButtonStatus,
        layoutConfig: ButtonHudLayoutConfig,
        disabled: Boolean
    ) {
        val layout = ButtonHudLayout(layoutConfig, config.padding)
        val offset = config.align.alignOffset(drawContext.scaledWindowSize, config.align, layout.size)

        fun drawButton(layout: ButtonLayout, clicked: Boolean) {
            val color = if (!disabled && clicked) {
                Colors.RED
            } else {
                Colors.BLACK
            }
            drawContext.fill(
                offset.x + layout.offset.x,
                offset.y + layout.offset.y,
                offset.x + layout.offset.x + layout.size.width,
                offset.y + layout.offset.y + layout.size.height,
                color
            )
        }

        // Forward
        drawButton(layout.forward, status.forward)
        // Backward
        drawButton(layout.backward, status.backward)
        // Left
        drawButton(layout.left, status.left)
        // Right
        drawButton(layout.right, status.right)
    }

    private fun drawJoystick(
        drawContext: DrawContext,
        config: ControllerHudConfig,
        status: JoystickStatus,
        layout: JoystickHudLayoutConfig,
        disabled: Boolean
    ) {
        // TODO
    }

    fun render(drawContext: DrawContext, tickCounter: RenderTickCounter) {
        val client = MinecraftClient.getInstance()
        val disabled = client.currentScreen != null

        val state = viewModel.state.value
        when (val layout = state.config.layout) {
            is ButtonHudLayoutConfig -> drawButton(drawContext, state.config, state.status.button, layout, disabled)
            is JoystickHudLayoutConfig -> drawJoystick(drawContext, state.config, state.status.joystick, layout, disabled)
        }
    }
}