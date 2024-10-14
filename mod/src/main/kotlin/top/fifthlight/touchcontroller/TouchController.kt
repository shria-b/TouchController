package top.fifthlight.touchcontroller

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import org.koin.core.Koin
import org.koin.core.context.startKoin
import org.koin.logger.slf4jLogger
import org.slf4j.LoggerFactory
import top.fifthlight.touchcontroller.config.TouchControllerConfigHolder
import top.fifthlight.touchcontroller.di.appModule
import top.fifthlight.touchcontroller.event.KeyboardInputEvents
import top.fifthlight.touchcontroller.proxy.LauncherSocketProxy
import top.fifthlight.touchcontroller.proxy.createClientProxy
import top.fifthlight.touchcontroller.proxy.message.VersionMessage
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback as FabricHudRenderCallback
import top.fifthlight.touchcontroller.event.HudRenderCallback as TouchControllerHudRenderCallback

private val controllerLogger = LoggerFactory.getLogger("TouchController")

data class SocketProxyHolder(
	var socketProxy: LauncherSocketProxy? = null
)

object TouchController : ClientModInitializer {
	const val NAMESPACE = "touchcontroller"

	override fun onInitializeClient() {
		controllerLogger.info("Loading TouchController…")

		val app = startKoin {
			slf4jLogger()
			modules(appModule)
		}

		val socketProxyHolder: SocketProxyHolder = app.koin.get()
		createClientProxy()?.apply {
			socketProxyHolder.socketProxy = this
			runBlocking {
				send(VersionMessage("1.0.0"))
			}
			@OptIn(DelicateCoroutinesApi::class)
			GlobalScope.launch {
				start()
			}
		}

		app.koin.initialize()
	}

	private fun Koin.initialize() {
		controllerLogger.info("Client proxy set, initialize mod")
		val configHolder: TouchControllerConfigHolder = get()
		configHolder.load()
		FabricHudRenderCallback.EVENT.register(get())
		TouchControllerHudRenderCallback.CROSSHAIR.register(get())
		WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register(get())
		WorldRenderEvents.START.register(get())
		KeyboardInputEvents.END_INPUT_TICK.register(get())
	}
}
