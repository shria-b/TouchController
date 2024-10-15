package top.fifthlight.touchcontroller.config.widget

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder
import net.minecraft.client.gui.widget.ClickableWidget
import net.minecraft.client.sound.PositionedSoundInstance
import net.minecraft.client.sound.SoundManager
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.Colors
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import top.fifthlight.touchcontroller.config.TouchControllerConfig
import top.fifthlight.touchcontroller.config.control.ControllerWidgetConfig
import top.fifthlight.touchcontroller.ext.withTranslate
import top.fifthlight.touchcontroller.layout.Align.*
import top.fifthlight.touchcontroller.layout.Context
import top.fifthlight.touchcontroller.layout.ContextStatus
import top.fifthlight.touchcontroller.layout.withAlign
import top.fifthlight.touchcontroller.proxy.data.IntOffset
import top.fifthlight.touchcontroller.proxy.data.IntSize
import top.fifthlight.touchcontroller.proxy.data.Offset

class LayoutEditor(
    x: Int = 0,
    y: Int = 0,
    width: Int = 0,
    height: Int = 0,
    message: Text? = null,
    private val config: TouchControllerConfig,
    private val onWidgetSelected: (LayoutEditor, ControllerWidgetConfig?) -> Unit
) : ClickableWidget(x, y, width, height, message), KoinComponent {
    private val client: MinecraftClient by inject()
    var selectedConfig: ControllerWidgetConfig? = null

    override fun renderWidget(drawContext: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        val context = Context(
            drawContext = drawContext,
            size = IntSize(width, height),
            screenOffset = IntOffset(x, y),
            scale = client.window.scaleFactor.toFloat(),
            pointers = emptyMap(),
            status = ContextStatus()
        )
        drawContext.withTranslate(x.toFloat(), y.toFloat()) {
            config.widgetConfigs.forEach { config ->
                context.withAlign(
                    align = config.align,
                    offset = config.offset,
                    size = config.size()
                ) {
                    config.render(this)
                    if (selectedConfig == config) {
                        context.drawContext.drawBorder(0, 0, size.width, size.height, Colors.WHITE)
                    }
                }
            }
        }
    }

    private var dragStart = Offset.ZERO
    private var origOffset = IntOffset.ZERO

    override fun onClick(mouseX: Double, mouseY: Double) {
        val pointerPosition = IntOffset(x = mouseX.toInt() - x, y = mouseY.toInt() - y)
        val screenSize = IntSize(width, height)

        for (widget in config.widgetConfigs) {
            val size = widget.size()
            val offset = widget.align.alignOffset(screenSize, size, widget.offset)
            val pointerOffset = pointerPosition - offset
            if (pointerOffset in size) {
                dragStart = Offset(mouseX.toFloat(), mouseY.toFloat())
                origOffset = widget.offset
                client.soundManager.play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0f))
                onWidgetSelected(this, widget)
                return
            }
        }
        onWidgetSelected(this, null)
    }

    override fun onDrag(mouseX: Double, mouseY: Double, deltaX: Double, deltaY: Double) {
        val config = selectedConfig ?: return
        val dragPosition = Offset(mouseX.toFloat(), mouseY.toFloat()) - dragStart
        config.offset = when (config.align) {
            LEFT_TOP -> IntOffset(
                x = origOffset.x + dragPosition.x.toInt(),
                y = origOffset.y + dragPosition.y.toInt()
            )
            RIGHT_TOP -> IntOffset(
                x = origOffset.x - dragPosition.x.toInt(),
                y = origOffset.y + dragPosition.y.toInt()
            )
            LEFT_BOTTOM -> IntOffset(
                x = origOffset.x + dragPosition.x.toInt(),
                y = origOffset.y - dragPosition.y.toInt()
            )
            RIGHT_BOTTOM -> IntOffset(
                x = origOffset.x - dragPosition.x.toInt(),
                y = origOffset.y - dragPosition.y.toInt()
            )
        }
    }

    override fun playDownSound(soundManager: SoundManager) {}

    override fun appendClickableNarrations(builder: NarrationMessageBuilder) {

    }
}
