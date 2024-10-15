package top.fifthlight.touchcontroller.model

import org.koin.core.component.KoinComponent
import top.fifthlight.touchcontroller.layout.ContextStatus

class ControllerHudModel : KoinComponent {
    var state = ContextStatus()
}
