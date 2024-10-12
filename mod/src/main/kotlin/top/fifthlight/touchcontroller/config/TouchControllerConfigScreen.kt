package top.fifthlight.touchcontroller.config

import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget
import net.minecraft.screen.ScreenTexts
import top.fifthlight.touchcontroller.asset.Texts

class TouchControllerConfigScreen(
    private val parent: Screen
) : Screen(
    Texts.OPTIONS_SCREEN_TITLE
) {
    private val layout = ThreePartsLayoutWidget(this)

    override fun init() {
        layout.addHeader(Texts.OPTIONS_SCREEN_TITLE, textRenderer)
        layout.addFooter(ButtonWidget.builder(ScreenTexts.DONE) { close() }.build())
        layout.forEachChild { element ->
            addDrawableChild(element)
        }
        initTabNavigation()
    }

    override fun initTabNavigation() {
        layout.refreshPositions()
    }

    override fun close() {
        client?.setScreen(parent)
    }
}