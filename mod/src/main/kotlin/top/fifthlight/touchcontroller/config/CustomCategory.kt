package top.fifthlight.touchcontroller.config

import com.google.common.collect.ImmutableList
import dev.isxander.yacl3.api.ConfigCategory
import dev.isxander.yacl3.api.CustomTabProvider
import dev.isxander.yacl3.api.OptionGroup
import dev.isxander.yacl3.gui.YACLScreen
import net.minecraft.client.gui.ScreenRect
import net.minecraft.client.gui.tab.Tab
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.ClickableWidget
import net.minecraft.client.gui.widget.Positioner
import net.minecraft.text.Text
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import top.fifthlight.touchcontroller.config.widget.BorderLayout
import top.fifthlight.touchcontroller.config.widget.LayoutEditor
import top.fifthlight.touchcontroller.config.widget.PropertiesPanel
import top.fifthlight.touchcontroller.config.widget.WidgetList
import java.util.function.Consumer

private class CustomTab(
    @JvmField
    val title: Text,
    tabArea: ScreenRect,
    screen: YACLScreen,
    config: TouchControllerConfig
) : Tab, KoinComponent {
    override fun getTitle(): Text = title

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
            onWidgetAdd = { config.widgetConfigs.add(it.copy()) }
        ).also { widgetList ->
            setFirstElement(widgetList) { _, width, height ->
                widgetList.width = width
                widgetList.height = height
            }
        }
        val propertiesPanel = PropertiesPanel(client = get(), width = 128, itemHeight = 24, itemPadding = 16)
        LayoutEditor(config = config, onWidgetSelected = { editor, config ->
            editor.selectedConfig = config
            propertiesPanel.selectedConfig = config
        }).also { layoutEditor ->
            setCenterElement(layoutEditor) { _, width, height ->
                layoutEditor.setDimensions(width, height)
            }
        }
        BorderLayout(width = 200, direction = BorderLayout.Direction.VERTICAL).apply {
            setCenterElement(propertiesPanel) { _, width, height ->
                propertiesPanel.setDimensions(width, height)
            }
            ButtonWidget.Builder(Text.literal("Save")) {
                screen.cancelOrReset()
            }.build().also { saveButton ->
                setSecondElement(saveButton) { _, width, height ->
                    saveButton.setDimensions(width, height)
                }
            }
        }.also { rightPanel ->
            setSecondElement(
                widget = rightPanel,
                positioner = Positioner.create().margin(8, 8)
            ) { _, width, height ->
                rightPanel.setDimension(width, height)
            }
        }
        refreshPositions()
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
    private val tooltip: Text,
    private val config: TouchControllerConfig
) : ConfigCategory, CustomTabProvider {
    override fun name(): Text = name

    override fun groups(): ImmutableList<OptionGroup> = ImmutableList.of()

    override fun tooltip(): Text = tooltip

    override fun createTab(screen: YACLScreen, tabArea: ScreenRect): Tab = CustomTab(name, tabArea, screen, config)
}