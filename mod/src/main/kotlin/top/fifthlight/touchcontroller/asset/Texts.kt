package top.fifthlight.touchcontroller.asset

import net.minecraft.text.MutableText
import net.minecraft.text.Text

object Texts {
    private const val PREFIX = "touch_controller"

    val OPTIONS_SCREEN: MutableText = Text.translatable("$PREFIX.screen.options")
    val OPTIONS_SCREEN_TITLE: MutableText = Text.translatable("$PREFIX.screen.options.title")
}