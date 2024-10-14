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
import kotlin.io.path.readText
import kotlin.io.path.writeText

class TouchControllerConfigHolder: KoinComponent {
    private val fabricLoader: FabricLoader = get()
    private val logger = LoggerFactory.getLogger(TouchControllerConfig::class.java)
    private val configFile = fabricLoader.configDir.resolve("touch_controller.json")
    private val json: Json by inject()
    private var configLoaded = atomic(false)
    private var _config = MutableStateFlow(TouchControllerConfig())
    val config = _config.asStateFlow()

    fun load() {
        if (configLoaded.compareAndSet(
            expect = false,
            update = true
        )) {
            @OptIn(DelicateCoroutinesApi::class)
            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    try {
                        _config.value = json.decodeFromString(configFile.readText())
                    } catch (ex: Exception) {
                        logger.warn("Failed to read config: ", ex)
                    }
                }
            }
        }
    }

    fun save(config: TouchControllerConfig) {
        _config.value = config
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
                        configFile.writeText(json.encodeToString(_config.value))
                    } catch (ex: Exception) {
                        logger.warn("Failed to write config: ", ex)
                    }
                }
            }
        }
    }
}