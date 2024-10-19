package top.fifthlight.touchcontroller.config.widget

import kotlinx.collections.immutable.PersistentList
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.Element
import net.minecraft.client.gui.Selectable
import net.minecraft.client.gui.widget.ElementListWidget
import top.fifthlight.touchcontroller.config.ObservableValue
import top.fifthlight.touchcontroller.config.TouchControllerLayout
import top.fifthlight.touchcontroller.config.control.ControllerWidgetConfig
import top.fifthlight.touchcontroller.config.control.ControllerWidgetConfig.Property
import top.fifthlight.touchcontroller.config.control.ControllerWidgetConfig.PropertyWidget
import top.fifthlight.touchcontroller.config.replaceItem
import top.fifthlight.touchcontroller.proxy.data.IntSize

class PropertiesPanelEntry<Config : ControllerWidgetConfig>(
    initialConfig: Config,
    widgetSize: IntSize,
    property: Property<Config, *>,
    propertyEditProvider: ControllerWidgetConfig.PropertyEditProvider<Config>
) : ElementListWidget.Entry<PropertiesPanelEntry<*>>() {
    private val propertyWidget: PropertyWidget<Config, *> = property.createController(propertyEditProvider)
    private val widget = propertyWidget.createWidget(initialConfig, widgetSize)

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
        widget.setPosition(x, y)
        widget.setDimensions(entryWidth, entryHeight)
        widget.render(context, mouseX, mouseY, tickDelta)
    }

    override fun children(): List<Element> = listOf(widget)

    override fun selectableChildren(): List<Selectable> = listOf(widget)
}

class PropertiesPanel(
    client: MinecraftClient,
    width: Int,
    height: Int = 0,
    y: Int = 0,
    itemHeight: Int,
    private val itemPadding: Int,
    private val layoutConfig: ObservableValue<TouchControllerLayout>,
    private val selectedConfig: ObservableValue<ControllerWidgetConfig?>,
) : ElementListWidget<PropertiesPanelEntry<*>>(
    client, width, height, y, itemHeight,
) {
    private fun <Config : ControllerWidgetConfig> createEntry(config: Config, property: Property<Config, *>) =
        PropertiesPanelEntry(
            initialConfig = config,
            widgetSize = IntSize(rowWidth, itemHeight),
            property = property,
            propertyEditProvider = object : ControllerWidgetConfig.PropertyEditProvider<Config> {
                @Suppress("UNCHECKED_CAST")
                override val currentConfig = selectedConfig.value as Config

                override fun newConfig(config: Config) {
                    layoutConfig.replaceItem(oldItem = selectedConfig.value ?: return, newItem = config)
                }
            }
        )

    private var selectedProperties: PersistentList<Property<ControllerWidgetConfig, *>>? = null
    init {
        selectedConfig.addListener { newConfig ->
            val properties = newConfig?.properties
            if (selectedProperties != properties) {
                clearEntries()
                val config = newConfig ?: return@addListener
                properties?.forEach {
                    addEntry(createEntry(config, it))
                }
            }
            selectedProperties = properties
        }
    }

    override fun getRowWidth(): Int = width - itemPadding
}