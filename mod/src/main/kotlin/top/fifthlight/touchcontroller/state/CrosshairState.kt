package top.fifthlight.touchcontroller.state

import top.fifthlight.touchcontroller.proxy.data.Offset

data class CrosshairState(
    val config: CrosshairConfig = CrosshairConfig(),
    val status: CrosshairStatus = CrosshairStatus(),
)

data class CrosshairConfig(
    val radius: Int = 36,
    val outerRadius: Int = 2,
)

data class CrosshairStatus(
    val enabled: Boolean = true,
    val position: Offset = Offset(left = 128f, top = 128f),
    val breakPercent: Float = .5f,
)