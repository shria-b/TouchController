package top.fifthlight.touchcontroller.config.control

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.plus
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.text.Text
import top.fifthlight.touchcontroller.asset.Texts
import top.fifthlight.touchcontroller.layout.Align
import top.fifthlight.touchcontroller.layout.Context
import top.fifthlight.touchcontroller.layout.Joystick
import top.fifthlight.touchcontroller.proxy.data.IntOffset
import top.fifthlight.touchcontroller.proxy.data.IntSize

@Serializable
data class JoystickConfig(
    var size: Float = 1f,
    var stickSize: Float = 1f,
    override val align: Align = Align.LEFT_BOTTOM,
    override val offset: IntOffset = IntOffset.ZERO,
    override val opacity: Float = 1f
) : ControllerWidgetConfig() {
    @Suppress("UNCHECKED_CAST")
    @Transient
    override val properties = super.properties + persistentListOf<Property<JoystickConfig, *>>(
        FloatProperty(
            getValue = { it.size },
            setValue = { config, value -> config.copy(size = value) },
            startValue = .5f,
            endValue = 2f,
            messageFormatter = { Text.translatable(Texts.OPTIONS_WIDGET_JOYSTICK_SIZE, it) },
        ),
        FloatProperty(
            getValue = { it.stickSize },
            setValue = { config, value -> config.copy(stickSize = value) },
            startValue = .5f,
            endValue = 2f,
            messageFormatter = { Text.translatable(Texts.OPTIONS_WIDGET_JOYSTICK_STICK_SIZE, it) },
        ),
    ) as PersistentList<Property<ControllerWidgetConfig, *>>

    override fun size(): IntSize = IntSize((size * 72).toInt())

    fun stickSize() = IntSize((stickSize * 48).toInt())

    override fun render(context: Context) {
        context.Joystick(this@JoystickConfig)
    }

    override fun cloneBase(align: Align, offset: IntOffset, opacity: Float) = copy(
        align = align,
        offset = offset,
        opacity = opacity
    )
}