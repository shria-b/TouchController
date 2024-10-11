package top.fifthlight.touchcontroller.model

import org.koin.core.component.KoinComponent
import top.fifthlight.touchcontroller.layout.ContextStatus
import top.fifthlight.touchcontroller.state.ControllerHudConfig

class ControllerHudModel : KoinComponent {
    var config = ControllerHudConfig()
    var state = ContextStatus()
}
