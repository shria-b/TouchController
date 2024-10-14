package top.fifthlight.touchcontroller.asset

import net.minecraft.text.MutableText
import net.minecraft.text.Text
import top.fifthlight.touchcontroller.TouchController

object Texts {
    private fun translatable(id: String): MutableText = Text.translatable("${TouchController.NAMESPACE}.$id")

    val OPTIONS_SCREEN = translatable("screen.options")
    val OPTIONS_SCREEN_TITLE = translatable("screen.options.title")
}