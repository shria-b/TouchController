package top.fifthlight.touchcontroller.state

import top.fifthlight.touchcontroller.layout.Align

data class ControllerHudConfig(
    val padding: Int = 8,
    val opacity: Float = 1f,
    val align: Align = Align.LEFT_BOTTOM,
    val enableSecondaryHand: Boolean = true,
    val lockSneak: Boolean = true,
    val layout: ControllerHudLayoutConfig = DPadHudLayoutConfig()
)

sealed class ControllerHudLayoutConfig

data class DPadHudLayoutConfig(
    val size: Int = 48
) : ControllerHudLayoutConfig()

data class JoystickHudLayoutConfig(
    val size: Int = 72,
    val stickSize: Int = 16
) : ControllerHudLayoutConfig()
