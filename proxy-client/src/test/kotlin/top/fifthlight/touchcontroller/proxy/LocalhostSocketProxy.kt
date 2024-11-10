package top.fifthlight.touchcontroller.proxy

import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.nio.channels.ServerSocketChannel
import kotlin.random.Random
import kotlin.random.nextInt

private val logger = LoggerFactory.getLogger("LocalhostSocketProxy")

fun localhostServerLauncherSocketProxy(): Pair<Int, LauncherSocketProxy> {
    val socket = ServerSocketChannel.open()
    for (i in 1..16) {
        val port = Random.nextInt(32768..65535)
        logger.trace("Select port $port")
        try {
            socket.bind(InetSocketAddress(InetAddress.getLoopbackAddress(), port))
            val proxy = ServerLauncherSocketProxy(ServerSocketChannelProxy(socket))
            return Pair(port, proxy)
        } catch (ex: IOException) {
            continue
        }
    }
    throw IOException("No port available")
}