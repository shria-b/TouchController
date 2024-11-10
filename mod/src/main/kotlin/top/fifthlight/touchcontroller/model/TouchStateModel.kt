package top.fifthlight.touchcontroller.model

import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text
import org.koin.core.component.KoinComponent
import top.fifthlight.touchcontroller.proxy.data.Offset
import top.fifthlight.touchcontroller.state.Pointer
import top.fifthlight.touchcontroller.state.PointerState

class TouchStateModel : KoinComponent {
    val pointers = HashMap<Int, Pointer>()

    fun addPointer(index: Int, position: Offset) {
        MinecraftClient.getInstance().inGameHud.chatHud.addMessage(Text.literal("Add index: $index"))
        pointers[index]?.let { pointer ->
            pointer.position = position
        } ?: run {
            pointers[index] = Pointer(position = position)
        }
    }

    fun removePointer(index: Int) {
        MinecraftClient.getInstance().inGameHud.chatHud.addMessage(Text.literal("Remove index: $index"))
        val pointer = pointers[index] ?: return
        if (pointer.state !is PointerState.Released) {
            pointer.state = PointerState.Released(previousPosition = pointer.position, previousState = pointer.state)
        }
    }

    fun clearPointer() {
        MinecraftClient.getInstance().inGameHud.chatHud.addMessage(Text.literal("Clear all"))
        pointers.forEach { (_, pointer) ->
            pointer.state = PointerState.Released(previousPosition = pointer.position, previousState = pointer.state)
        }
    }
}