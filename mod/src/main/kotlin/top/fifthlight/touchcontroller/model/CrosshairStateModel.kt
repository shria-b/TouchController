package top.fifthlight.touchcontroller.model

import org.koin.core.component.KoinComponent
import top.fifthlight.touchcontroller.state.CrosshairState
import top.fifthlight.touchcontroller.state.CrosshairStatus

class CrosshairStateModel: KoinComponent {
    var state = CrosshairState()
        private set

    fun updateStatus(status: CrosshairStatus?) {
        state = state.copy(status = status)
    }
}