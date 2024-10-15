package top.fifthlight.touchcontroller.config.widget

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.Element
import net.minecraft.client.gui.Selectable
import net.minecraft.client.gui.widget.ElementListWidget
import top.fifthlight.touchcontroller.config.control.ControllerWidgetConfig
import top.fifthlight.touchcontroller.proxy.data.IntSize
import kotlin.properties.Delegates

class PropertiesPanel(
    client: MinecraftClient,
    width: Int,
    height: Int = 0,
    y: Int = 0,
    itemHeight: Int,
    private val itemPadding: Int,
) : ElementListWidget<PropertiesPanel.Entry>(
    client, width, height, y, itemHeight,
) {
    var selectedConfig: ControllerWidgetConfig? by Delegates.observable(null) { _, _, newConfig ->
        clearEntries()
        newConfig?.properties(IntSize(rowWidth, itemHeight))?.forEach {
            addEntry(Entry(it))
        }
    }

    override fun getRowWidth(): Int = width - itemPadding

    class Entry(
        private val property: ControllerWidgetConfig.Property<*>
    ) : ElementListWidget.Entry<Entry>() {
        override fun render(
            context: DrawContext,
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
            property.widget.setPosition(x, y)
            property.widget.setDimensions(entryWidth, entryHeight)
            property.widget.render(context, mouseX, mouseY, tickDelta)
        }

        override fun children(): List<Element> = listOf(property.widget)

        override fun selectableChildren(): List<Selectable> = listOf(property.widget)
    }
}