package top.fifthlight.touchcontroller.model

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import top.fifthlight.touchcontroller.layout.ButtonHudLayout
import top.fifthlight.touchcontroller.layout.ButtonLayout
import top.fifthlight.touchcontroller.state.*

class ControllerHudModel : KoinComponent {
    private val touchStateModel by inject<TouchStateModel>()
    private val globalStateModel by inject<GlobalStateModel>()
    private val _config = MutableStateFlow(ControllerHudConfig())
    val config = _config.asStateFlow()
    private val _status = MutableStateFlow(ControllerHudStatus())
    val status = _status.asStateFlow()

    init {
        @OptIn(DelicateCoroutinesApi::class)
        GlobalScope.launch {
            touchStateModel.state.combine(globalStateModel.state, ::Pair)
                .combine(config) { (touch, global), config -> Triple(touch, global, config) }
                .map { (touch, global, config) ->
                    when (val layoutConfig = config.layout) {
                        is ButtonHudLayoutConfig -> {
                            val layout = ButtonHudLayout(layoutConfig, config.padding)
                            val offset = config.align.alignOffset(global.windowSize, layout.size)

                            fun anyPointerInButton(layout: ButtonLayout): Boolean = touch.pointers.any {
                                val scaledOffset = it.position / global.scaleFactor
                                scaledOffset.inRegion(offset + layout.offset, layout.size)
                            }

                            ControllerHudStatus(
                                button = ButtonStatus(
                                    forward = anyPointerInButton(layout.forward),
                                    backward = anyPointerInButton(layout.backward),
                                    left = anyPointerInButton(layout.left),
                                    right = anyPointerInButton(layout.right),
                                )
                            )
                        }

                        is JoystickHudLayoutConfig -> {
                            // TODO
                            ControllerHudStatus(
                                joystick = JoystickStatus(
                                    x = 0f,
                                    y = 0f
                                )
                            )
                        }
                    }
                }.collect {
                    _status.value = it
                }
        }
    }
}
