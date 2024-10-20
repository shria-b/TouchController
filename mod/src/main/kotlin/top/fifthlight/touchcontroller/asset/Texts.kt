package top.fifthlight.touchcontroller.asset

import net.minecraft.text.MutableText
import net.minecraft.text.Text
import top.fifthlight.touchcontroller.TouchController

object Texts {
    private fun key(id: String) = "${TouchController.NAMESPACE}.$id"
    private fun translatable(id: String): MutableText = Text.translatable(key(id))

    val OPTIONS_SCREEN = translatable("screen.options")
    val OPTIONS_SCREEN_TITLE = translatable("screen.options.title")

    val OPTIONS_CATEGORY_GLOBAL_TITLE = translatable("screen.options.category.global.title")
    val OPTIONS_CATEGORY_GLOBAL_TOOLTIP = translatable("screen.options.category.global.tooltip")

    val OPTIONS_CATEGORY_GLOBAL_DISABLE_MOUSE_TITLE = translatable("screen.options.category.global.disable_mouse.title")
    val OPTIONS_CATEGORY_GLOBAL_DISABLE_MOUSE_DESCRIPTION =
        translatable("screen.options.category.global.disable_mouse.description")

    val OPTIONS_CATEGORY_GLOBAL_DISABLE_MOUSE_LOCK_TITLE = translatable("screen.options.category.global.disable_cursor_lock.title")
    val OPTIONS_CATEGORY_GLOBAL_DISABLE_MOUSE_LOCK_DESCRIPTION =
        translatable("screen.options.category.global.disable_cursor_lock.description")

    val OPTIONS_CATEGORY_GLOBAL_ENABLE_TOUCH_EMULATION_TITLE = translatable("screen.options.category.global.enable_touch_emulation.title")
    val OPTIONS_CATEGORY_GLOBAL_ENABLE_TOUCH_EMULATION_DESCRIPTION =
        translatable("screen.options.category.global.enable_touch_emulation.description")

    val OPTIONS_CATEGORY_CUSTOM_TITLE = translatable("screen.options.category.custom.title")
    val OPTIONS_CATEGORY_CUSTOM_TOOLTIP = translatable("screen.options.category.custom.tooltip")
    
    val OPTIONS_SAVE_TITLE = translatable("screen.options.save.title")
    val OPTIONS_SAVE_TOOLTIP = translatable("screen.options.save.tooltip")
    val OPTIONS_FINISH_TITLE = translatable("screen.options.finish.title")
    val OPTIONS_FINISH_TOOLTIP = translatable("screen.options.finish.tooltip")
    val OPTIONS_RESET_TITLE = translatable("screen.options.reset.title")
    val OPTIONS_RESET_TOOLTIP = translatable("screen.options.reset.tooltip")
    val OPTIONS_UNDO_TITLE = translatable("screen.options.undo.title")
    val OPTIONS_UNDO_TOOLTIP = translatable("screen.options.undo.tooltip")
    val OPTIONS_CANCEL_TITLE = translatable("screen.options.cancel.title")
    val OPTIONS_CANCEL_TOOLTIP = translatable("screen.options.cancel.tooltip")
    val OPTIONS_REMOVE_TITLE = translatable("screen.options.remove.title")
    val OPTIONS_REMOVE_TOOLTIP = translatable("screen.options.remove.tooltip")

    val OPTIONS_WIDGET_GENERAL_PROPERTY_ALIGN_TOP_LEFT = translatable("screen.options.widget.general.property.top.left")
    val OPTIONS_WIDGET_GENERAL_PROPERTY_ALIGN_TOP_RIGHT = translatable("screen.options.widget.general.property.top.right")
    val OPTIONS_WIDGET_GENERAL_PROPERTY_ALIGN_BOTTOM_LEFT = translatable("screen.options.widget.general.property.bottom.left")
    val OPTIONS_WIDGET_GENERAL_PROPERTY_ALIGN_BOTTOM_RIGHT = translatable("screen.options.widget.general.property.bottom.right")
    val OPTIONS_WIDGET_GENERAL_PROPERTY_OPACITY = key("screen.options.widget.general.property.opacity")

    val OPTIONS_WIDGET_DPAD_PROPERTY_CLASSIC = translatable("screen.options.widget.dpad.property.classic")
    val OPTIONS_WIDGET_DPAD_PROPERTY_SIZE = key("screen.options.widget.dpad.property.size")
    val OPTIONS_WIDGET_DPAD_PROPERTY_PADDING = key("screen.options.widget.dpad.property.padding")

    val OPTIONS_WIDGET_JOYSTICK_SIZE = key("screen.options.widget.joystick.property.size")
    val OPTIONS_WIDGET_JOYSTICK_STICK_SIZE = key("screen.options.widget.joystick.property.stick_size")
}