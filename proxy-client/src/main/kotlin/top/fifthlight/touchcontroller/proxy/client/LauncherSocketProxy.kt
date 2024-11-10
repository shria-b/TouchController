package top.fifthlight.touchcontroller.proxy.client

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runInterruptible
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import top.fifthlight.touchcontroller.proxy.LauncherSocketProxy
import java.io.IOException
import java.net.SocketAddress
import java.net.UnixDomainSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel
import java.nio.file.Path

private val logger = LoggerFactory.getLogger(ClientLauncherSocketProxy::class.java)

class ClientLauncherSocketProxy(
    private val channel: SocketChannel
): LauncherSocketProxy() {
    constructor(address: SocketAddress): this(SocketChannel.open().also { it.connect(address) })

    private suspend fun startSend() {
        sendQueue.collect { message ->
            val buffer = ByteBuffer.allocate(1024)
            message.encode(buffer)
            buffer.flip()

            withContext(Dispatchers.IO) {
                runInterruptible { channel.write(buffer) }
            }
        }
    }

    override suspend fun start() {
        coroutineScope {
            launch {
                logger.info("Client send started")
                startSend()
            }
            launch {
                _connected.value = true
                logger.info("Client receive started")
                receiveChannel(channel::read)
            }
        }
    }
}

fun unixSocketClientLauncherSocketProxy(path: Path? = null): LauncherSocketProxy? {
    val socketPath = path ?: System.getenv("TOUCH_CONTROLLER_PROXY")?.let {Path.of(it)} ?: return null
    return try {
        ClientLauncherSocketProxy(UnixDomainSocketAddress.of(socketPath))
    } catch (ex: IOException) {
        logger.warn("Failed to open unix socket at $socketPath", ex)
        null
    }
}
