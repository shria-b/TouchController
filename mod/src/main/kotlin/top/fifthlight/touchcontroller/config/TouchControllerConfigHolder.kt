package top.fifthlight.touchcontroller.config

import kotlinx.atomicfu.atomic
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.fabricmc.loader.api.FabricLoader
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import org.slf4j.LoggerFactory
import top.fifthlight.touchcontroller.TouchController
import java.nio.file.FileAlreadyExistsException
import kotlin.io.path.createDirectory
import kotlin.io.path.readText
import kotlin.io.path.writeText

class TouchControllerConfigHolder : KoinComponent {
    private val fabricLoader: FabricLoader = get()
    private val logger = LoggerFactory.getLogger(TouchControllerConfig::class.java)
    private val configDir = fabricLoader.configDir.resolve(TouchController.NAMESPACE)
    private val configFile = configDir.resolve("touch_controller.json")
    private val layoutFile = configDir.resolve("touch_controller_layout.json")

    private val json: Json by inject()
    private var configLoaded = atomic(false)
    private val _config = MutableStateFlow(TouchControllerConfig())
    val config = _config.asStateFlow()
    private val _layout = MutableStateFlow(defaultTouchControllerLayout)
    val layout = _layout.asStateFlow()

    fun load() {
        if (configLoaded.compareAndSet(expect = false,update = true)) {
            @OptIn(DelicateCoroutinesApi::class)
            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    try {
                        _config.value = json.decodeFromString(configFile.readText())
                        _layout.value = json.decodeFromString(layoutFile.readText())
                    } catch (ex: Exception) {
                        logger.warn("Failed to read config: ", ex)
                    }
                }
            }
        }
    }

    fun saveConfig(config: TouchControllerConfig) {
        _config.value = config
    }

    fun saveLayout(layout: TouchControllerLayout) {
        _layout.value = layout
    }

    init {
        @OptIn(DelicateCoroutinesApi::class)
        GlobalScope.launch {
            config.collectLatest {
                if (!configLoaded.value) {
                    return@collectLatest
                }
                withContext(Dispatchers.IO) {
                    try {
                        try {
                            configDir.createDirectory()
                        } catch (_: FileAlreadyExistsException) {
                        }
                        configFile.writeText(json.encodeToString(_config.value))
                        layoutFile.writeText(json.encodeToString(_layout.value))
                    } catch (ex: Exception) {
                        logger.warn("Failed to write config: ", ex)
                    }
                }
            }
        }
    }
}