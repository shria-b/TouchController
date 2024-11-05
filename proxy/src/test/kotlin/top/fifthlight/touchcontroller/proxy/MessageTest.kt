package top.fifthlight.touchcontroller.proxy

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import java.nio.file.Path
import java.util.*

abstract class MessageTest {
    @Rule
    @JvmField
    val folder = TemporaryFolder()

    private fun createSocketPath(): Path {
        val name = UUID.randomUUID().toString()
        return folder.root.toPath().resolve(name)
    }

    fun createProxyPair(): Pair<LauncherSocketProxy, LauncherSocketProxy> {
        val path = createSocketPath()
        val serverProxy = createServerProxy(path)
        val clientProxy = createClientProxy(path) ?: error("Failed to create client proxy")
        return Pair(serverProxy, clientProxy)
    }
}