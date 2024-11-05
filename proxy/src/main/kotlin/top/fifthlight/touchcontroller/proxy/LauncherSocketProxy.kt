package top.fifthlight.touchcontroller.proxy

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.slf4j.LoggerFactory
import top.fifthlight.touchcontroller.proxy.message.MessageDecodeException
import top.fifthlight.touchcontroller.proxy.message.ProxyMessage
import top.fifthlight.touchcontroller.proxy.message.VersionMessage
import top.fifthlight.touchcontroller.proxy.message.decodeMessage
import java.io.IOException
import java.net.StandardProtocolFamily
import java.net.UnixDomainSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.ClosedByInterruptException
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.nio.file.Path

private val logger = LoggerFactory.getLogger(LauncherSocketProxy::class.java)

abstract class LauncherSocketProxy {
    private val receiveLock = Mutex()
    private val receiveQueue = mutableListOf<ProxyMessage>()
    protected val _connected = MutableStateFlow(false)
    val connected = _connected.asStateFlow()
    protected val sendQueue = MutableSharedFlow<ProxyMessage>(0, 256)

    protected suspend fun receiveChannel(channel: SocketChannel) {
        withContext(Dispatchers.IO.limitedParallelism(1)) {
            val buffer = ByteBuffer.allocate(1024)
            while (true) {
                buffer.clear()
                val length = try {
                    runInterruptible { channel.read(buffer) }
                } catch (ex: ClosedByInterruptException) {
                    throw CancellationException()
                }
                if (length == -1) {
                    break
                }
                buffer.flip()

                if (buffer.remaining() < 8) {
                    logger.warn("Bad message size: $buffer")
                    continue
                }

                val type = buffer.getInt()
                val message = try {
                    decodeMessage(type, buffer)
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

    suspend fun receive(block: (ProxyMessage) -> Unit) {
        receiveLock.withLock {
            receiveQueue.forEach(block)
            receiveQueue.clear()
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
                receiveChannel(channel)
            }
        }
    }
}

fun createClientProxy(path: Path? = null): LauncherSocketProxy? {
    val socketPath = path ?: System.getenv("TOUCH_CONTROLLER_PROXY")?.let {Path.of(it)} ?: return null
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
            logger.info("Server send started")
            launch {
                sendQueue.collect { message ->
                    val buffer = ByteBuffer.allocate(1024)
                    message.encode(buffer)
                    buffer.flip()
                    val len = buffer.limit()
                    for (client in clients) {
                        runInterruptible { client.write(buffer) }
                        buffer.clear()
                        buffer.limit(len)
                    }
                }
            }
            while (true) {
                val client = try {
                    runInterruptible { channel.accept() }
                } catch (ex: ClosedByInterruptException) {
                    throw CancellationException()
                }
                launch {
                    clientLock.withLock {
                        if (clients.isEmpty()) {
                            _connected.value = true
                        }
                        clients.add(client)
                    }
                    logger.info("Server receive started")
                    receiveChannel(client)
                    clientLock.withLock {
                        clients.remove(client)
                        if (clients.isEmpty()) {
                            _connected.value = false
                        }
                    }
                }
            }
        }
    }
}

fun createServerProxy(path: Path): LauncherSocketProxy {
    val socketAddress = UnixDomainSocketAddress.of(path)
    val socket = ServerSocketChannel.open(StandardProtocolFamily.UNIX)
    socket.bind(socketAddress)
    return ServerLauncherSocketProxy(socket)
}
