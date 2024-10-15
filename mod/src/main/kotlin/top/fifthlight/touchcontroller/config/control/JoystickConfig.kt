package top.fifthlight.touchcontroller.config.control

import kotlinx.serialization.Serializable
import top.fifthlight.touchcontroller.asset.Texts
import top.fifthlight.touchcontroller.layout.Context
import top.fifthlight.touchcontroller.layout.Joystick
import top.fifthlight.touchcontroller.proxy.data.IntSize

@Serializable
data class JoystickConfig(
    var size: Float = 1f,
    var stickSize: Float = 1f,
): ControllerWidgetConfig() {
    override fun properties(widgetSize: IntSize) = super.properties(widgetSize) + listOf(
        FloatProperty(
            initialValue = size,
            startValue = .5f,
            endValue = 2f,
            messageKey = Texts.OPTIONS_WIDGET_JOYSTICK_SIZE,
            onChange = { size = it },
            widgetSize = widgetSize
        ),
        FloatProperty(
            initialValue = stickSize,
            startValue = .5f,
            endValue = 2f,
            messageKey = Texts.OPTIONS_WIDGET_JOYSTICK_STICK_SIZE,
            onChange = { stickSize = it },
            widgetSize = widgetSize
        ),
    )

    override fun size(): IntSize = IntSize((size * 72).toInt())

    override fun render(context: Context) {
        context.Joystick(this@JoystickConfig)
    }

    override fun copy(): JoystickConfig = JoystickConfig(
        size, stickSize
    ).apply {
        offset = this@JoystickConfig.offset
        align = this@JoystickConfig.align
        opacity = this@JoystickConfig.opacity
    }
}