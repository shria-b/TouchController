package top.fifthlight.touchcontroller.config

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable
import top.fifthlight.touchcontroller.config.control.ControllerWidgetConfig
import top.fifthlight.touchcontroller.config.control.DPadConfig
import top.fifthlight.touchcontroller.layout.Align
import top.fifthlight.touchcontroller.proxy.data.IntOffset

@Serializable
data class TouchControllerConfig(
    val disableMouse: Boolean = true,
    val disableMouseLock: Boolean = true,
    val disableCrosshair: Boolean = true,
    val enableTouchEmulation: Boolean = false,
)

typealias TouchControllerLayout = PersistentList<ControllerWidgetConfig>

val defaultTouchControllerLayout: TouchControllerLayout = persistentListOf(
    DPadConfig(
        align = Align.LEFT_BOTTOM,
        offset = IntOffset(8, 8)
    )
)