package top.fifthlight.touchcontroller.config

import kotlinx.serialization.Serializable

@Serializable
data class TouchControllerConfig(
    val disableMouse: Boolean = true,
    val disableMouseLock: Boolean = true,
    val enableTouchEmulation: Boolean = false,
)
