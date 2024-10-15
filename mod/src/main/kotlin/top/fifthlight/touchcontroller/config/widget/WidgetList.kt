package top.fifthlight.touchcontroller.config.widget

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.Element
import net.minecraft.client.gui.Selectable
import net.minecraft.client.gui.widget.ElementListWidget
import net.minecraft.util.Colors
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import top.fifthlight.touchcontroller.config.control.ControllerWidgetConfig
import top.fifthlight.touchcontroller.config.control.DPadConfig
import top.fifthlight.touchcontroller.config.control.JoystickConfig
import top.fifthlight.touchcontroller.ext.withScale
import top.fifthlight.touchcontroller.ext.withTranslate
import top.fifthlight.touchcontroller.layout.Context
import top.fifthlight.touchcontroller.layout.ContextStatus
import top.fifthlight.touchcontroller.proxy.data.IntOffset
import top.fifthlight.touchcontroller.proxy.data.IntSize

class WidgetList(
    client: MinecraftClient,
    width: Int,
    height: Int = 0,
    y: Int = 0,
    itemHeight: Int,
    private val itemPadding: Int,
    onWidgetAdd: (ControllerWidgetConfig) -> Unit,
) : ElementListWidget<WidgetList.Entry>(
    client, width, height, y, itemHeight
) {
    companion object {
        private val DEFAULT_CONFIGS = listOf(
            DPadConfig(),
            JoystickConfig(),
        )
    }

    init {
        DEFAULT_CONFIGS.forEach {
            addEntry(Entry(
                config = it,
                onClicked = { onWidgetAdd(it) }
            ))
        }
    }

    override fun getRowWidth(): Int = width - itemPadding

    class Entry(
        private val config: ControllerWidgetConfig,
        private val onClicked: () -> Unit
    ) : ElementListWidget.Entry<Entry>(), KoinComponent {
        private val client: MinecraftClient = get()
        override fun render(
            drawContext: DrawContext,
            index: Int,
            y: Int,
            x: Int,
            entryWidth: Int,
            entryHeight: Int,
            mouseX: Int,
            mouseY: Int,
            hovered: Boolean,
            tickDelta: Float
        ) {
            val widgetSize = config.size()
            val entrySize = IntSize(entryWidth, entryHeight)

            val widthFactor = if (widgetSize.width > entrySize.width) {
                entrySize.width.toFloat() / widgetSize.width.toFloat()
            } else 1f
            val heightFactor = if (widgetSize.height > entrySize.height) {
                entrySize.height.toFloat() / widgetSize.height.toFloat()
            } else 1f
            val componentScaleFactor = widthFactor.coerceAtMost(heightFactor)
            val displaySize = (widgetSize.toSize() * componentScaleFactor).toIntSize()
            val offset = IntOffset(x, y) + (entrySize - displaySize) / 2

            val context = Context(
                drawContext = drawContext,
                size = widgetSize,
                screenOffset = offset,
                scale = client.window.scaleFactor.toFloat() * componentScaleFactor,
                pointers = emptyMap(),
                status = ContextStatus()
            )
            drawContext.withTranslate(offset.toOffset()) {
                drawContext.withScale(componentScaleFactor) {
                    config.render(context)
                }
            }

            if (index > 0) {
                drawContext.drawHorizontalLine(x, x + entryWidth, y, Colors.WHITE)
            }
        }

        override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
            onClicked()
            return true
        }

        override fun children() = emptyList<Element>()

        override fun selectableChildren() = emptyList<Selectable>()
    }
}