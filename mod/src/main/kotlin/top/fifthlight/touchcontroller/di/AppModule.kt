package top.fifthlight.touchcontroller.di

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.StartTick
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import org.koin.dsl.module
import top.fifthlight.touchcontroller.SocketProxyHolder
import top.fifthlight.touchcontroller.event.KeyboardInputEvents
import top.fifthlight.touchcontroller.handler.ClientTickStartCallback
import top.fifthlight.touchcontroller.handler.HudCallbackHandler
import top.fifthlight.touchcontroller.handler.KeyboardInputHandler
import top.fifthlight.touchcontroller.model.ControllerHudModel
import top.fifthlight.touchcontroller.model.TouchStateModel

val appModule = module {
    single { SocketProxyHolder() }
    single<HudRenderCallback> { HudCallbackHandler() }
    single<StartTick> { ClientTickStartCallback() }
    single<KeyboardInputEvents.EndInputTick> { KeyboardInputHandler() }
    single { ControllerHudModel() }
    single { TouchStateModel() }
}