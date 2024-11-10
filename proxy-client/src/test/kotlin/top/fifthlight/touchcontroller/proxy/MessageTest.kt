package top.fifthlight.touchcontroller.proxy

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import top.fifthlight.touchcontroller.proxy.client.ClientLauncherSocketProxy
import top.fifthlight.touchcontroller.proxy.client.unixSocketClientLauncherSocketProxy
import java.net.InetAddress
import java.net.InetSocketAddress
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
        if (System.getProperty("os.name").contains("windows", true)) {
            val (port, serverProxy) = localhostServerLauncherSocketProxy()
            val clientProxy = ClientLauncherSocketProxy(InetSocketAddress(InetAddress.getLoopbackAddress(), port))
            return Pair(serverProxy, clientProxy)
        } else {
            val path = createSocketPath()
            val serverProxy = unixSocketServerLauncherSocketProxy(path)
            val clientProxy = unixSocketClientLauncherSocketProxy(path) ?: error("Failed to create client proxy")
            return Pair(serverProxy, clientProxy)
        }
    }
}