package top.fifthlight.touchcontroller.config.control

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.client.gui.widget.ClickableWidget
import net.minecraft.text.Text
import top.fifthlight.touchcontroller.asset.Texts
import top.fifthlight.touchcontroller.layout.Align
import top.fifthlight.touchcontroller.layout.Context
import top.fifthlight.touchcontroller.proxy.data.IntOffset
import top.fifthlight.touchcontroller.proxy.data.IntSize
import kotlin.math.round

@Serializable
abstract class ControllerWidgetConfig {
    abstract val align: Align
    abstract val offset: IntOffset
    abstract val opacity: Float

    interface PropertyEditProvider<Config> {
        val currentConfig: Config
        fun newConfig(config: Config)
    }

    interface Property<Config : ControllerWidgetConfig, Value, Widget : ClickableWidget> {
        fun createController(editProvider: PropertyEditProvider<Config>): PropertyWidget<Config, Widget>
    }

    interface PropertyWidget<Config : ControllerWidgetConfig, Widget : ClickableWidget> {
        fun createWidget(initialConfig: Config, size: IntSize): Widget
        fun updateWidget(config: Config, widget: Widget)
    }

    companion object {
        private val _properties = persistentListOf<Property<ControllerWidgetConfig, *, *>>(
            EnumProperty(
                getValue = { it.align },
                setValue = { config, value -> config.cloneBase(align = value) },
                items = listOf(
                    Align.LEFT_TOP to Texts.OPTIONS_WIDGET_GENERAL_PROPERTY_ALIGN_TOP_LEFT,
                    Align.LEFT_BOTTOM to Texts.OPTIONS_WIDGET_GENERAL_PROPERTY_ALIGN_BOTTOM_LEFT,
                    Align.RIGHT_TOP to Texts.OPTIONS_WIDGET_GENERAL_PROPERTY_ALIGN_TOP_RIGHT,
                    Align.RIGHT_BOTTOM to Texts.OPTIONS_WIDGET_GENERAL_PROPERTY_ALIGN_BOTTOM_RIGHT,
                ),
            ),
            FloatProperty(
                getValue = { it.opacity },
                setValue = { config, value -> config.cloneBase(opacity = value) },
                messageFormatter = { opacity ->
                    Text.translatable(
                        Texts.OPTIONS_WIDGET_GENERAL_PROPERTY_OPACITY,
                        round(opacity * 100f).toInt().toString()
                    )
                }
            )
        )
    }

    @Transient
    open val properties: PersistentList<Property<ControllerWidgetConfig, *, *>> = _properties

    abstract fun size(): IntSize

    abstract fun render(context: Context)

    abstract fun cloneBase(
        align: Align = this.align,
        offset: IntOffset = this.offset,
        opacity: Float = this.opacity,
    ): ControllerWidgetConfig
}
