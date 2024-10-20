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
import top.fifthlight.touchcontroller.layout.DPad
import top.fifthlight.touchcontroller.proxy.data.IntOffset
import top.fifthlight.touchcontroller.proxy.data.IntSize
import kotlin.math.round

enum class DPadExtraButton {
    NONE,
    SNEAK,
    JUMP
}

@Serializable
data class DPadConfig(
    val classic: Boolean = true,
    val size: Float = 2f,
    val padding: Int = if (classic) 4 else -1,
    val extraButton: DPadExtraButton = DPadExtraButton.SNEAK,
    override val align: Align = Align.LEFT_BOTTOM,
    override val offset: IntOffset = IntOffset.ZERO,
    override val opacity: Float = 1f
) : ControllerWidgetConfig() {
    companion object {
        private val _properties = persistentListOf<Property<DPadConfig, *, *>>(
            EnumProperty(
                getValue = { it.extraButton },
                setValue = { config, value -> config.copy(extraButton = value) },
                items = listOf(
                    DPadExtraButton.NONE to Texts.OPTIONS_WIDGET_DPAD_PROPERTY_EXTRA_BUTTON_NONE,
                    DPadExtraButton.SNEAK to Texts.OPTIONS_WIDGET_DPAD_PROPERTY_EXTRA_BUTTON_JUMP,
                    DPadExtraButton.JUMP to Texts.OPTIONS_WIDGET_DPAD_PROPERTY_EXTRA_BUTTON_SNEAK,
                ),
            ),
            FloatProperty(
                getValue = { it.size },
                setValue = { config, value -> config.copy(size = value) },
                startValue = .5f,
                endValue = 4f,
                messageFormatter = {
                    Text.translatable(
                        Texts.OPTIONS_WIDGET_DPAD_PROPERTY_SIZE,
                        round(it * 100f).toString()
                    )
                },
            ),
            IntProperty(
                getValue = { it.padding },
                setValue = { config, value -> config.copy(padding = value) },
                range = -1..16,
                messageFormatter = { Text.translatable(Texts.OPTIONS_WIDGET_DPAD_PROPERTY_PADDING, it) }
            ),
            BooleanProperty(
                getValue = { it.classic },
                setValue = { config, value -> config.copy(classic = value) },
                message = Texts.OPTIONS_WIDGET_DPAD_PROPERTY_CLASSIC,
            )
        )
    }

    @Suppress("UNCHECKED_CAST")
    @Transient
    override val properties = super.properties + _properties as PersistentList<Property<ControllerWidgetConfig, *, *>>

    fun buttonSize(): IntSize = IntSize((22f * size).toInt(), (22f * size).toInt())
    fun buttonPadding(): Int = (padding * size).toInt()

    override fun size(): IntSize = buttonSize() * 3 + buttonPadding() * 2

    override fun render(context: Context) = context.DPad(this@DPadConfig)

    override fun cloneBase(align: Align, offset: IntOffset, opacity: Float) = copy(
        align = align,
        offset = offset,
        opacity = opacity
    )
}
