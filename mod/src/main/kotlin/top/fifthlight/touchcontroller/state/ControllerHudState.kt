package top.fifthlight.touchcontroller.state

import top.fifthlight.touchcontroller.proxy.data.IntOffset
import top.fifthlight.touchcontroller.proxy.data.IntSize

data class ControllerHudConfig(
    val padding: Int = 8,
    val opacity: Float = 1f,
    val align: ControllerHudAlign = ControllerHudAlign.LEFT_BOTTOM,
    val enableSecondaryHand: Boolean = true,
    val lockSneak: Boolean = true,
    val layout: ControllerHudLayoutConfig = ButtonHudLayoutConfig()
)

enum class ControllerHudAlign {
    LEFT_TOP,
    RIGHT_TOP,
    LEFT_BOTTOM,
    RIGHT_BOTTOM;

    fun alignOffset(windowSize: IntSize, size: IntSize): IntOffset {
        return when (this) {
            LEFT_TOP -> IntOffset(
                left = 0,
                top = 0,
            )
            RIGHT_TOP -> IntOffset(
                left = windowSize.width - size.width,
                top = 0,
            )
            LEFT_BOTTOM -> IntOffset(
                left = 0,
                top = windowSize.height - size.height,
            )
            RIGHT_BOTTOM -> IntOffset(
                left = windowSize.width - size.width,
                top = windowSize.height - size.height,
            )
        }
    }
}

sealed class ControllerHudLayoutConfig

data class ButtonHudLayoutConfig(
    val size: Int = 48
): ControllerHudLayoutConfig()

data class JoystickHudLayoutConfig(
    val size: Int = 48
): ControllerHudLayoutConfig()

data class ControllerHudStatus(
    val button: ButtonStatus = ButtonStatus(),
    val joystick: JoystickStatus = JoystickStatus(),
    val sneak: Boolean = false
)

data class ButtonStatus(
    val forward: Boolean = false,
    val backward: Boolean = false,
    val left: Boolean = false,
    val right: Boolean = false,
)

data class JoystickStatus(
    val x: Float = 0f,
    val y: Float = 0f
)
