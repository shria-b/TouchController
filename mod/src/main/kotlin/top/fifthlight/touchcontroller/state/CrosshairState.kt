package top.fifthlight.touchcontroller.state

import top.fifthlight.touchcontroller.proxy.data.Offset

data class CrosshairState(
    val config: CrosshairConfig = CrosshairConfig(),
    val status: CrosshairStatus? = null,
)

data class CrosshairConfig(
    val radius: Int = 36,
    val outerRadius: Int = 2,
)

data class CrosshairStatus(
    val position: Offset,
    val breakPercent: Float,
)