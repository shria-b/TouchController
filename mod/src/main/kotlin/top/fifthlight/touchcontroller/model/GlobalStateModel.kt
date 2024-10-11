package top.fifthlight.touchcontroller.model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import net.minecraft.client.MinecraftClient
import top.fifthlight.touchcontroller.ext.scaledSize
import top.fifthlight.touchcontroller.state.GlobalState

class GlobalStateModel {
    private val _state = MutableStateFlow(GlobalState())
    val state = _state.asStateFlow()

    fun update(client: MinecraftClient) {
        _state.update { state ->
            state.copy(
                inGame = client.currentScreen == null,
                windowSize = client.window.scaledSize
            )
        }
    }
}
