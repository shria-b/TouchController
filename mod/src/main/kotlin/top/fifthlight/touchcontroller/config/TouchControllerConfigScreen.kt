package top.fifthlight.touchcontroller.config

import dev.isxander.yacl3.api.ConfigCategory
import dev.isxander.yacl3.api.YetAnotherConfigLib
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text

fun TouchControllerConfigScreen(parent: Screen): Screen {
    return YetAnotherConfigLib.createBuilder()
        .title(Text.literal("Used for narration. Could be used to render a title in the future."))
        .category(ConfigCategory.createBuilder().name(Text.literal("Group")).build())
        .build()
        .generateScreen(parent)
}