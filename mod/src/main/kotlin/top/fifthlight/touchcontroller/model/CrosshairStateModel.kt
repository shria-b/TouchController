package top.fifthlight.touchcontroller.model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.component.KoinComponent
import top.fifthlight.touchcontroller.state.CrosshairState

class CrosshairStateModel: KoinComponent {
    private val _state = MutableStateFlow(CrosshairState())
    val state = _state.asStateFlow()


}