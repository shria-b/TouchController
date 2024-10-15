package top.fifthlight.touchcontroller.config.control

import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.CheckboxWidget
import net.minecraft.client.gui.widget.SliderWidget
import net.minecraft.text.Text
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import top.fifthlight.touchcontroller.proxy.data.IntSize
import kotlin.math.round
import kotlin.properties.Delegates

class BooleanProperty(
    initialValue: Boolean,
    onChange: (Boolean) -> Unit,
    message: Text,
    widgetSize: IntSize
) : ControllerWidgetConfig.Property<Boolean>(), KoinComponent {
    override var value: Boolean by Delegates.observable(initialValue) { _, _, new ->
        onChange(new)
    }

    override val widget: CheckboxWidget = CheckboxWidget.builder(message, get())
        .maxWidth(widgetSize.width)
        .checked(initialValue)
        .callback { _, checked -> value = checked }
        .build()
}

class EnumProperty<T>(
    initialValue: T,
    private val items: List<Pair<T, Text>>,
    onChange: (T) -> Unit,
    widgetSize: IntSize
) : ControllerWidgetConfig.Property<T>() {
    override var value: T by Delegates.observable(initialValue) { _, _, new ->
        widget.message = getItemText(new)
        onChange(new)
    }

    private fun getItemText(item: T): Text =
        items.firstOrNull { it.first == item }?.second ?: Text.literal(item.toString())

    override val widget: ButtonWidget = ButtonWidget
        .builder(getItemText(initialValue)) {
            if (items.isEmpty()) {
                return@builder
            }
            val index = (items.indexOfFirst { it.first == value } + 1) % items.size
            value = items[index].first
        }
        .size(widgetSize.width, widgetSize.height)
        .build()
}

class FloatProperty(
    initialValue: Float,
    onChange: (Float) -> Unit,
    private val startValue: Float = 0f,
    private val endValue: Float = 1f,
    private val messageKey: String,
    private val valueConverter: (Float) -> String = { round(it * 100f).toInt().toString() + "%" },
    widgetSize: IntSize
) : ControllerWidgetConfig.Property<Float>() {
    override var value: Float by Delegates.observable(initialValue) { _, _, new ->
        onChange(new)
    }

    private val currentMessage
        get() = Text.translatable(messageKey, valueConverter(value))

    override val widget: SliderWidget =
        object : SliderWidget(
            0,
            0,
            widgetSize.width,
            widgetSize.height,
            currentMessage,
            ((initialValue - startValue) / (endValue - startValue)).toDouble()
        ) {
            override fun updateMessage() {
                this.message = currentMessage
            }

            override fun applyValue() {
                this@FloatProperty.value = this.value.toFloat() * (endValue - startValue) + startValue
            }
        }
}

class IntProperty(
    initialValue: Int,
    onChange: (Int) -> Unit,
    private val range: IntRange,
    private val messageKey: String,
    private val valueConverter: (Int) -> String = Int::toString,
    widgetSize: IntSize
) : ControllerWidgetConfig.Property<Int>() {
    override var value: Int by Delegates.observable(initialValue) { _, _, new ->
        onChange(new)
    }

    private val currentMessage
        get() = Text.translatable(messageKey, valueConverter(value))

    override val widget: SliderWidget =
        object : SliderWidget(
            0,
            0,
            widgetSize.width,
            widgetSize.height,
            currentMessage,
            (initialValue - range.first).toDouble() / (range.last - range.first + 1).toDouble()
        ) {
            override fun updateMessage() {
                this.message = currentMessage
            }

            override fun applyValue() {
                this@IntProperty.value = (this.value * (range.last - range.first + 1).toDouble()).toInt() + range.first
            }
        }
}