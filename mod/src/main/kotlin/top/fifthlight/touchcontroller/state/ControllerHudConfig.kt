package top.fifthlight.touchcontroller.state

import top.fifthlight.touchcontroller.layout.Align

data class ControllerHudConfig(
    val layout: ControllerHudLayoutConfig = DPadHudLayoutConfig()
)

sealed class ControllerHudLayoutConfig

data class DPadHudLayoutConfig(
    val align: Align = Align.LEFT_BOTTOM,
    val opacity: Float = 1f,
    val classic: Boolean = true,
    val padding: Int = 8,
    val size: Int = 44,
) : ControllerHudLayoutConfig()

data class JoystickHudLayoutConfig(
    val align: Align = Align.LEFT_BOTTOM,
    val padding: Int = 8,
    val size: Int = 72,
    val stickSize: Int = 16
) : ControllerHudLayoutConfig()
