package top.fifthlight.touchcontroller.config.control

import kotlinx.serialization.Serializable
import top.fifthlight.touchcontroller.asset.Texts
import top.fifthlight.touchcontroller.layout.Context
import top.fifthlight.touchcontroller.layout.DPad
import top.fifthlight.touchcontroller.proxy.data.IntSize

@Serializable
class DPadConfig(
    var classic: Boolean = true,
    var size: Float = 1f,
    var padding: Int = 4,
) : ControllerWidgetConfig() {
    override fun properties(widgetSize: IntSize) = super.properties(widgetSize) + listOf(
        FloatProperty(
            initialValue = size,
            startValue = .5f,
            endValue = 2f,
            messageKey = Texts.OPTIONS_WIDGET_DPAD_PROPERTY_SIZE,
            onChange = { size = it },
            widgetSize = widgetSize
        ),
        IntProperty(
            initialValue = padding,
            range = 0..16,
            messageKey = Texts.OPTIONS_WIDGET_DPAD_PROPERTY_PADDING,
            onChange = { padding = it },
            widgetSize = widgetSize
        ),
        BooleanProperty(
            initialValue = classic,
            onChange = { classic = it },
            message = Texts.OPTIONS_WIDGET_DPAD_PROPERTY_CLASSIC,
            widgetSize = widgetSize
        )
    )

    fun buttonSize(): IntSize = IntSize((22f * size).toInt() * 3, (22f * size).toInt() * 3)

    override fun size(): IntSize = buttonSize() * 3 + padding * 2

    override fun render(context: Context) {
        context.DPad(this@DPadConfig)
    }

    override fun copy(): DPadConfig = DPadConfig(
        classic, size, padding
    ).apply {
        offset = this@DPadConfig.offset
        align = this@DPadConfig.align
        opacity = this@DPadConfig.opacity
    }
}
