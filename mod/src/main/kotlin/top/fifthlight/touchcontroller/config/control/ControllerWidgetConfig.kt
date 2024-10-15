package top.fifthlight.touchcontroller.config.control

import kotlinx.serialization.Serializable
import net.minecraft.client.gui.widget.ClickableWidget
import top.fifthlight.touchcontroller.asset.Texts
import top.fifthlight.touchcontroller.layout.Align
import top.fifthlight.touchcontroller.layout.Context
import top.fifthlight.touchcontroller.proxy.data.IntOffset
import top.fifthlight.touchcontroller.proxy.data.IntSize

@Serializable
sealed class ControllerWidgetConfig(
    var align: Align = Align.LEFT_TOP,
    var offset: IntOffset = IntOffset.ZERO,
    var opacity: Float = 1f
) {
    sealed class Property<Value> {
        abstract var value: Value
        abstract val widget: ClickableWidget
    }

    open fun properties(widgetSize: IntSize) = listOf<Property<*>>(
        EnumProperty(
            initialValue = align,
            items = listOf(
                Align.LEFT_TOP to Texts.OPTIONS_WIDGET_GENERAL_PROPERTY_ALIGN_TOP_LEFT,
                Align.LEFT_BOTTOM to Texts.OPTIONS_WIDGET_GENERAL_PROPERTY_ALIGN_BOTTOM_LEFT,
                Align.RIGHT_TOP to Texts.OPTIONS_WIDGET_GENERAL_PROPERTY_ALIGN_TOP_RIGHT,
                Align.RIGHT_BOTTOM to Texts.OPTIONS_WIDGET_GENERAL_PROPERTY_ALIGN_BOTTOM_RIGHT,
            ),
            onChange = { align = it },
            widgetSize = widgetSize
        ),
        FloatProperty(
            initialValue = opacity,
            onChange = { opacity = it },
            messageKey = Texts.OPTIONS_WIDGET_GENERAL_PROPERTY_OPACITY,
            widgetSize = widgetSize
        )
    )

    abstract fun size(): IntSize

    abstract fun render(context: Context)

    abstract fun copy(): ControllerWidgetConfig
}
