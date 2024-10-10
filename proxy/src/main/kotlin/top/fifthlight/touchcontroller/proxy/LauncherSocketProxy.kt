package top.fifthlight.touchcontroller.proxy

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.slf4j.LoggerFactory
import top.fifthlight.touchcontroller.proxy.message.MessageDecodeException
import top.fifthlight.touchcontroller.proxy.message.ProxyMessage
import top.fifthlight.touchcontroller.proxy.message.VersionMessage
import top.fifthlight.touchcontroller.proxy.message.decodeMessage
import java.io.IOException
import java.net.UnixDomainSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.nio.file.Path
import java.util.*

private val logger = LoggerFactory.getLogger(LauncherSocketProxy::class.java)

abstract class LauncherSocketProxy {
    private val receiveLock = Mutex()
    private val receiveQueue = ArrayDeque<ProxyMessage>()
    protected val sendQueue = MutableSharedFlow<ProxyMessage>(0, 256)

    protected suspend fun receiveChannel(channel: SocketChannel) {
        withContext(Dispatchers.IO.limitedParallelism(1)) {
            val buffer = ByteBuffer.allocate(1024)
            while (true) {
                buffer.clear()
                if (channel.read(buffer) == 0) {
                    break
                }
                buffer.flip()

                if (buffer.remaining() < 8) {
                    logger.warn("Bad message size: $buffer")
                    continue
                }

                val type = buffer.getInt()
                val payload = ByteArray(buffer.remaining())
                val message = try {
                    decodeMessage(type, payload)
                } catch (ex: MessageDecodeException) {
                    logger.warn("Failed to decode message", ex)
                    continue
                }
                when (message) {
                    is VersionMessage -> {
                        logger.info("Proxy side version: ${message.version}")
                    }
                    else -> {
                        receiveLock.withLock {
                            receiveQueue.add(message)
                        }
                    }
                }
            }
        }
    }

    fun trySend(message: ProxyMessage) {
        sendQueue.tryEmit(message)
    }

    suspend fun send(message: ProxyMessage) {
        sendQueue.emit(message)
    }

    fun receive(block: (ProxyMessage) -> Unit) {
        runBlocking {
            receiveLock.withLock {
                receiveQueue.forEach(block)
                receiveQueue.clear()
            }
        }
    }

    abstract suspend fun start()
}

private class ClientLauncherSocketProxy(
    private val channel: SocketChannel
): LauncherSocketProxy() {
    private suspend fun startSend() {
        sendQueue.collect { message ->
            val buffer = ByteBuffer.allocate(1024)
            message.encode(buffer)
            buffer.flip()

            withContext(Dispatchers.IO.limitedParallelism(1)) {
                channel.write(buffer)
            }
        }
    }

    override suspend fun start() {
        coroutineScope {
            launch {
                startSend()
            }
            launch {
                receiveChannel(channel)
            }
        }
    }
}

fun createClientProxy(): LauncherSocketProxy? {
    val socketPath = System.getenv("TOUCH_CONTROLLER_PROXY") ?: return null
    val socketAddress = UnixDomainSocketAddress.of(socketPath)
    val socket = try {
        SocketChannel.open(socketAddress)
    } catch (ex: IOException) {
        logger.warn("Failed to open unix socket at $socketPath", ex)
        return null
    }
    return ClientLauncherSocketProxy(socket)
}

private class ServerLauncherSocketProxy(
    private val channel: ServerSocketChannel
): LauncherSocketProxy() {
    override suspend fun start() {
        withContext(Dispatchers.IO.limitedParallelism(16)) {
            val clients = ArrayList<SocketChannel>()
            val clientLock = Mutex()
            launch {
                sendQueue.collect { message ->
                    val buffer = ByteBuffer.allocate(1024)
                    message.encode(buffer)
                    buffer.flip()
                    clients.forEach { client ->
                        client.write(buffer)
                    }
                }
            }
            while (true) {
                val client = channel.accept()
                launch {
                    clientLock.withLock {
                        clients.add(client)
                    }
                    receiveChannel(client)
                    clientLock.withLock {
                        clients.remove(client)
                    }
                }
            }
        }
    }
}

fun createServerProxy(path: Path): LauncherSocketProxy {
    val socketAddress = UnixDomainSocketAddress.of(path)
    val socket = ServerSocketChannel.open()
    socket.bind(socketAddress)
    return ServerLauncherSocketProxy(socket)
}
