package top.fifthlight.touchcontroller.config

import com.google.common.collect.ImmutableList
import dev.isxander.yacl3.api.*
import dev.isxander.yacl3.api.utils.Dimension
import dev.isxander.yacl3.api.utils.OptionUtils
import dev.isxander.yacl3.gui.YACLScreen
import kotlinx.collections.immutable.plus
import net.minecraft.client.gui.ScreenRect
import net.minecraft.client.gui.tab.Tab
import net.minecraft.client.gui.tooltip.Tooltip
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.ClickableWidget
import net.minecraft.client.gui.widget.GridWidget
import net.minecraft.client.gui.widget.Positioner
import net.minecraft.text.Text
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import top.fifthlight.touchcontroller.asset.Texts
import top.fifthlight.touchcontroller.config.control.ControllerWidgetConfig
import top.fifthlight.touchcontroller.config.widget.BorderLayout
import top.fifthlight.touchcontroller.config.widget.LayoutEditor
import top.fifthlight.touchcontroller.config.widget.PropertiesPanel
import top.fifthlight.touchcontroller.config.widget.WidgetList
import java.util.function.Consumer
import javax.tools.Tool

class ObservableValue<Value>(value: Value) {
    private val listeners = mutableListOf<(Value) -> Unit>()
    var value = value
        set(value) {
            val changed = field != value
            field = value
            if (changed) {
                listeners.forEach { it(value) }
            }
        }

    fun addListener(listener: (Value) -> Unit) = listeners.add(listener)
}

fun ObservableValue<TouchControllerLayout>.replaceItem(
    oldItem: ControllerWidgetConfig,
    newItem: ControllerWidgetConfig
) {
    val index = value.indexOf(oldItem).takeIf { it >= 0 } ?: return
    value = value.set(index, newItem)
}

private class CustomTab(
    @JvmField
    val title: Text,
    tabArea: ScreenRect,
    private val screen: YACLScreen,
    layoutConfig: ObservableValue<TouchControllerLayout>
) : Tab, KoinComponent {
    private val selectedConfig = ObservableValue<ControllerWidgetConfig?>(null)

    override fun getTitle(): Text = title

    private val saveFinishedButton: ButtonWidget
    private val cancelResetButton: ButtonWidget
    private val undoButton: ButtonWidget

    private val layout = BorderLayout(
        left = tabArea.left,
        top = tabArea.top,
        width = tabArea.width,
        height = tabArea.height,
        direction = BorderLayout.Direction.HORIZONTAL
    ).apply {
        WidgetList(
            client = get(),
            width = 148,
            itemHeight = 96,
            itemPadding = 16,
            onWidgetAdd = { widget ->
                layoutConfig.value += widget
            }
        ).also { widgetList ->
            setFirstElement(widgetList) { _, width, height ->
                widgetList.width = width
                widgetList.height = height
            }
        }
        val propertiesPanel = PropertiesPanel(
            client = get(),
            width = 128,
            itemHeight = 24,
            itemPadding = 16,
            layoutConfig = layoutConfig,
            selectedConfig = selectedConfig
        )
        LayoutEditor(layoutConfig = layoutConfig, selectedConfig = selectedConfig).also { layoutEditor ->
            setCenterElement(layoutEditor) { _, width, height ->
                layoutEditor.setDimensions(width, height)
            }
        }
        val rightPanelWidth = 200
        BorderLayout(width = rightPanelWidth, direction = BorderLayout.Direction.VERTICAL).apply {
            setCenterElement(propertiesPanel) { _, width, height ->
                propertiesPanel.setDimensions(width, height)
            }
            GridWidget().apply {
                val padding = 8
                val smallButtonWidth = (rightPanelWidth - padding) / 2
                val buttonHeight = 20
                setColumnSpacing(padding)
                setRowSpacing(padding)
                createAdder(2).apply {
                    ButtonWidget.builder(Text.of("Cancel")) {
                        screen.cancelOrReset()
                        updateButtons()
                    }.apply {
                        size(smallButtonWidth, buttonHeight)
                    }.build().also {
                        cancelResetButton = it
                        add(it)
                    }
                    ButtonWidget.builder(Texts.OPTIONS_UNDO_TITLE) {
                        screen.undo()
                        updateButtons()
                    }.apply {
                        tooltip(Tooltip.of(Texts.OPTIONS_UNDO_TOOLTIP))
                        size(smallButtonWidth, buttonHeight)
                    }.build().also {
                        undoButton = it
                        add(it)
                    }
                    ButtonWidget.builder(Text.literal("Finish")) {
                        screen.finishOrSave()
                        updateButtons()
                    }.apply {
                        size(rightPanelWidth, buttonHeight)
                    }.build().also {
                        saveFinishedButton = it
                        add(it, 2)
                    }
                }
                setSecondElement(this) { _, _, _ -> refreshPositions() }
            }
            ButtonWidget.Builder(Text.literal("Save")) {
                screen.cancelOrReset()
            }.build()
        }.also { rightPanel ->
            setSecondElement(
                widget = rightPanel,
                positioner = Positioner.create().margin(8, 8)
            ) { _, width, height ->
                rightPanel.setDimension(width, height)
            }
        }
        refreshPositions()
        updateButtons()
    }

    init {
        OptionUtils.forEachOptions(screen.config) { option ->
            option.addListener { _, _ ->
                updateButtons()
            }
        }
    }

    private fun updateButtons() {
        val pendingChanges = screen.pendingChanges()

        undoButton.active = pendingChanges
        if (pendingChanges) {
            saveFinishedButton.setMessage(Texts.OPTIONS_SAVE_TITLE)
            saveFinishedButton.setTooltip(Tooltip.of(Texts.OPTIONS_SAVE_TOOLTIP))
            cancelResetButton.setMessage(Texts.OPTIONS_CANCEL_TITLE)
            cancelResetButton.setTooltip(Tooltip.of(Texts.OPTIONS_CANCEL_TOOLTIP))
        } else {
            saveFinishedButton.setMessage(Texts.OPTIONS_FINISH_TITLE)
            saveFinishedButton.setTooltip(Tooltip.of(Texts.OPTIONS_FINISH_TOOLTIP))
            cancelResetButton.setMessage(Texts.OPTIONS_RESET_TITLE)
            cancelResetButton.setTooltip(Tooltip.of(Texts.OPTIONS_RESET_TOOLTIP))
        }
    }

    override fun forEachChild(consumer: Consumer<ClickableWidget>) = layout.forEachChild(consumer)

    override fun refreshGrid(tabArea: ScreenRect) {
        layout.setPosition(tabArea.left, tabArea.top)
        layout.setDimension(tabArea.width, tabArea.height)
        layout.refreshPositions()
    }
}

class CustomCategory(
    private val name: Text,
    private val tooltip: Text
) : ConfigCategory, CustomTabProvider, KoinComponent {
    private val configHolder: TouchControllerConfigHolder = get()
    private val currentConfig = configHolder.layout
    private val pendingConfig = ObservableValue(currentConfig.value)
    override fun name(): Text = name

    private val groups = ImmutableList.of(
        OptionGroup.createBuilder().apply {
            option(Option.createBuilder<TouchControllerLayout>().apply {
                name(Text.empty())
                description(OptionDescription.EMPTY)
                customController { option ->
                    object : Controller<TouchControllerLayout> {
                        override fun option() = option
                        override fun formatValue() = Text.empty()
                        override fun provideWidget(screen: YACLScreen, widgetDimension: Dimension<Int>) =
                            throw UnsupportedOperationException()
                    }
                }
                binding(defaultTouchControllerLayout, { currentConfig.value }, { configHolder.saveLayout(it) })
            }.build().also { option ->
                pendingConfig.addListener {
                    if (option.pendingValue() != it) {
                        option.requestSet(it)
                    }
                }
                option.addListener { _, configs ->
                    pendingConfig.value = configs
                }
            })
        }.build()
    )

    override fun groups(): ImmutableList<OptionGroup> = groups

    override fun tooltip(): Text = tooltip

    override fun createTab(screen: YACLScreen, tabArea: ScreenRect): Tab = CustomTab(
        title = name,
        tabArea = tabArea,
        screen = screen,
        layoutConfig = pendingConfig
    )
}