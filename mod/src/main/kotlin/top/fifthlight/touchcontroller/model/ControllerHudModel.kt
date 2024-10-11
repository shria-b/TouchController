package top.fifthlight.touchcontroller.model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import net.minecraft.client.MinecraftClient
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import top.fifthlight.touchcontroller.ext.scaledSize
import top.fifthlight.touchcontroller.layout.ButtonHudLayout
import top.fifthlight.touchcontroller.layout.ButtonLayout
import top.fifthlight.touchcontroller.state.ButtonHudLayoutConfig
import top.fifthlight.touchcontroller.state.ButtonStatus
import top.fifthlight.touchcontroller.state.ControllerHudState
import top.fifthlight.touchcontroller.state.JoystickHudLayoutConfig

class ControllerHudModel : KoinComponent {
    private val touchState by inject<TouchStateModel>()
    private val _state = MutableStateFlow(ControllerHudState())
    val state = _state.asStateFlow()

    fun refresh(client: MinecraftClient) {
        val window = client.window

        val hudState = state.value
        val touchState = touchState.state.value

        val windowSize = window.scaledSize
        when (val layoutConfig = hudState.config.layout) {
            is ButtonHudLayoutConfig -> {
                val layout = ButtonHudLayout(layoutConfig, hudState.config.padding)
                val offset = hudState.config.align.alignOffset(windowSize, layout.size)

                fun anyPointerInButton(layout: ButtonLayout): Boolean = touchState.pointers.any {
                    val scaledOffset = it.position / window.scaleFactor.toFloat()
                    scaledOffset.inRegion(offset + layout.offset, layout.size)
                }

                val buttonStatus = ButtonStatus(
                    forward = anyPointerInButton(layout.forward),
                    backward = anyPointerInButton(layout.backward),
                    left = anyPointerInButton(layout.left),
                    right = anyPointerInButton(layout.right),
                )

                client.inGameHud

                _state.getAndUpdate { state ->
                    state.copy(
                        status = state.status.copy(
                            button = buttonStatus
                        )
                    )
                }
            }

            is JoystickHudLayoutConfig -> {
                // TODO
            }
        }
    }
}
