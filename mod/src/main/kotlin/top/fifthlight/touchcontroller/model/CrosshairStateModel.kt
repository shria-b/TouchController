package top.fifthlight.touchcontroller.model

import org.koin.core.component.KoinComponent
import top.fifthlight.touchcontroller.state.CrosshairState

class CrosshairStateModel: KoinComponent {
    var state = CrosshairState()
        private set
}