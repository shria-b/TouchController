package top.fifthlight.touchcontroller

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import top.fifthlight.touchcontroller.config.TouchControllerConfigScreen

class TouchControllerModMenuApiImpl : ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<TouchControllerConfigScreen> =
        ConfigScreenFactory<TouchControllerConfigScreen> { parent -> TouchControllerConfigScreen(parent) }
}