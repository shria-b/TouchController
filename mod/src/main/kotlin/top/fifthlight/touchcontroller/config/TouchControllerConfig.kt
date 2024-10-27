package top.fifthlight.touchcontroller.config

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable
import top.fifthlight.touchcontroller.control.*
import top.fifthlight.touchcontroller.layout.Align
import top.fifthlight.touchcontroller.proxy.data.IntOffset

@Serializable
data class TouchControllerConfig(
    val disableMouse: Boolean = true,
    val disableMouseLock: Boolean = true,
    val disableCrosshair: Boolean = true,
    val enableTouchEmulation: Boolean = false,
    val crosshair: CrosshairConfig = CrosshairConfig()
)

@Serializable
data class CrosshairConfig(
    val radius: Int = 36,
    val outerRadius: Int = 2,
)

typealias TouchControllerLayout = PersistentList<ControllerWidget>

val defaultTouchControllerLayout: TouchControllerLayout = persistentListOf(
    DPad(
        align = Align.LEFT_BOTTOM,
        offset = IntOffset(8, 8)
    ),
    JumpButton(
        align = Align.RIGHT_BOTTOM,
        offset = IntOffset(42, 42)
    ),
    PauseButton(
        align = Align.CENTER_TOP,
        offset = IntOffset(-9, 0)
    ),
    ChatButton(
        align = Align.CENTER_TOP,
        offset = IntOffset(9, 0)
    )
)