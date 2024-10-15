package top.fifthlight.touchcontroller.di

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents.BeforeBlockOutline
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import org.koin.dsl.binds
import org.koin.dsl.module
import top.fifthlight.touchcontroller.SocketProxyHolder
import top.fifthlight.touchcontroller.config.TouchControllerConfigHolder
import top.fifthlight.touchcontroller.event.KeyboardInputEvents
import top.fifthlight.touchcontroller.handler.HudCallbackHandler
import top.fifthlight.touchcontroller.handler.KeyboardInputHandler
import top.fifthlight.touchcontroller.handler.WorldRendererHandler
import top.fifthlight.touchcontroller.model.ControllerHudModel
import top.fifthlight.touchcontroller.model.CrosshairStateModel
import top.fifthlight.touchcontroller.model.GlobalStateModel
import top.fifthlight.touchcontroller.model.TouchStateModel
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback as FabricHudRenderCallback
import top.fifthlight.touchcontroller.event.HudRenderCallback as TouchControllerHudRenderCallback

val appModule = module {
    single { MinecraftClient.getInstance() }
    single { FabricLoader.getInstance() }
    single { get<MinecraftClient>().textRenderer }
    single {
        @OptIn(ExperimentalSerializationApi::class)
        Json {
            encodeDefaults = false
            ignoreUnknownKeys = true
            allowComments = true
            allowTrailingComma = true
        }
    }
    single { TouchControllerConfigHolder() }
    single { SocketProxyHolder() }
    single<FabricHudRenderCallback> { HudCallbackHandler() }
    single<KeyboardInputEvents.EndInputTick> { KeyboardInputHandler() }
    single { WorldRendererHandler() } binds arrayOf(
        BeforeBlockOutline::class,
        TouchControllerHudRenderCallback.CrosshairRender::class,
        WorldRenderEvents.Start::class
    )
    single { GlobalStateModel() }
    single { ControllerHudModel() }
    single { TouchStateModel() }
    single { CrosshairStateModel() }
}