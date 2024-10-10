package top.fifthlight.touchcontroller.model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import org.koin.core.component.KoinComponent
import top.fifthlight.touchcontroller.state.PointerState
import top.fifthlight.touchcontroller.state.TouchState

class TouchStateModel: KoinComponent {
    private val _state = MutableStateFlow(TouchState())
    val state = _state.asStateFlow()

    fun addPointer(pointer: PointerState) {
        _state.getAndUpdate { state ->
            state.copy(
                pointers = state.pointers.filter { it.index != pointer.index } + pointer
            )
        }
    }

    fun removePointer(index: Int) {
        _state.getAndUpdate { state ->
            state.copy(
                pointers = state.pointers.filter { it.index != index }
            )
        }
    }

    fun clearPointer() {
        _state.getAndUpdate { state ->
            state.copy(
                pointers = listOf()
            )
        }
    }
}