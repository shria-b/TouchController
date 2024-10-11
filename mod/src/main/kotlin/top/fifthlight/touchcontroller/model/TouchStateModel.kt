package top.fifthlight.touchcontroller.model

import org.koin.core.component.KoinComponent
import top.fifthlight.touchcontroller.proxy.data.Offset
import top.fifthlight.touchcontroller.state.Pointer

class TouchStateModel : KoinComponent {
    val pointers = HashMap<Int, Pointer>()

    fun addPointer(index: Int, position: Offset) {
        pointers[index] = Pointer(position = position)
    }

    fun removePointer(index: Int) {
        pointers.remove(index)
    }

    fun clearPointer() {
        pointers.clear()
    }
}