package top.fifthlight.touchcontroller.state

import top.fifthlight.touchcontroller.layout.Align

data class ControllerHudConfig(
    val padding: Int = 8,
    val opacity: Float = 1f,
    val align: Align = Align.LEFT_BOTTOM,
    val enableSecondaryHand: Boolean = true,
    val lockSneak: Boolean = true,
    val layout: ControllerHudLayoutConfig = ButtonHudLayoutConfig()
)

sealed class ControllerHudLayoutConfig

data class ButtonHudLayoutConfig(
    val size: Int = 48
): ControllerHudLayoutConfig()

data class JoystickHudLayoutConfig(
    val size: Int = 48
): ControllerHudLayoutConfig()
