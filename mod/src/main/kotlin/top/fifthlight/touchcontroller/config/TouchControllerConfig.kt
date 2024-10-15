package top.fifthlight.touchcontroller.config

import kotlinx.serialization.Serializable
import top.fifthlight.touchcontroller.config.control.ControllerWidgetConfig

@Serializable
data class TouchControllerConfig(
    val disableMouse: Boolean = true,
    val disableMouseLock: Boolean = true,
    val enableTouchEmulation: Boolean = false,
    val widgetConfigs: ArrayList<ControllerWidgetConfig> = arrayListOf()
) {
    fun clone(
        disableMouse: Boolean = this.disableMouse,
        disableMouseLock: Boolean = this.disableMouseLock,
        enableTouchEmulation: Boolean = this.enableTouchEmulation,
        widgetConfigs: ArrayList<ControllerWidgetConfig> = this.widgetConfigs
    ) = TouchControllerConfig(
        disableMouse, disableMouseLock, enableTouchEmulation, ArrayList(widgetConfigs.map { it.copy() })
    )
}
