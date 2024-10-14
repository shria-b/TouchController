package top.fifthlight.touchcontroller.asset

import net.minecraft.text.MutableText
import net.minecraft.text.Text
import top.fifthlight.touchcontroller.TouchController

object Texts {
    private fun translatable(id: String): MutableText = Text.translatable("${TouchController.NAMESPACE}.$id")

    val OPTIONS_SCREEN = translatable("screen.options")
    val OPTIONS_SCREEN_TITLE = translatable("screen.options.title")

    val OPTIONS_CATEGORY_GLOBAL_TITLE = translatable("screen.options.category.global.title")

    val OPTIONS_CATEGORY_GLOBAL_DISABLE_MOUSE_TITLE = translatable("screen.options.category.global.disable_mouse.title")
    val OPTIONS_CATEGORY_GLOBAL_DISABLE_MOUSE_DESCRIPTION =
        translatable("screen.options.category.global.disable_mouse.description")

    val OPTIONS_CATEGORY_GLOBAL_DISABLE_MOUSE_LOCK_TITLE = translatable("screen.options.category.global.disable_cursor_lock.title")
    val OPTIONS_CATEGORY_GLOBAL_DISABLE_MOUSE_LOCK_DESCRIPTION =
        translatable("screen.options.category.global.disable_cursor_lock.description")

    val OPTIONS_CATEGORY_GLOBAL_ENABLE_TOUCH_EMULATION_TITLE = translatable("screen.options.category.global.enable_touch_emulation.title")
    val OPTIONS_CATEGORY_GLOBAL_ENABLE_TOUCH_EMULATION_DESCRIPTION =
        translatable("screen.options.category.global.enable_touch_emulation.description")
}