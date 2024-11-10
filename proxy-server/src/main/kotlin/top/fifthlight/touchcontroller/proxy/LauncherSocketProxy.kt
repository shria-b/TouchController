package top.fifthlight.touchcontroller.proxy

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runInterruptible
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import top.fifthlight.touchcontroller.proxy.message.MessageDecodeException
import top.fifthlight.touchcontroller.proxy.message.ProxyMessage
import top.fifthlight.touchcontroller.proxy.message.VersionMessage
import top.fifthlight.touchcontroller.proxy.message.decodeMessage
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.channels.ClosedByInterruptException

private val logger = LoggerFactory.getLogger(LauncherSocketProxy::class.java)

abstract class LauncherSocketProxy {
    private val receiveLock = Mutex()
    private val receiveQueue = mutableListOf<ProxyMessage>()
    protected val _connected = MutableStateFlow(false)
    val connected = _connected.asStateFlow()
    protected val sendQueue = MutableSharedFlow<ProxyMessage>(0, 256)

    protected suspend fun receiveChannel(receiver: (ByteBuffer) -> Int) {
        withContext(Dispatchers.IO.limitedParallelism(1)) {
            val buffer = ByteBuffer.allocate(1024)
            while (true) {
                buffer.clear()
                val length = try {
                    runInterruptible { receiver(buffer) }
                } catch (_: ClosedByInterruptException) {
                    throw kotlin.coroutines.cancellation.CancellationException()
                } catch (_: IOException) {
                    break
                }
                if (length <= 0) {
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

interface ServerSocket: AutoCloseable {
    fun accept(): Socket
}

interface Socket: AutoCloseable {
    fun read(buffer: ByteBuffer): Int
    fun write(buffer: ByteBuffer): Int
}

fun Socket.writeAll(buffer: ByteBuffer): Int {
    var written = 0
    while (buffer.hasRemaining()) {
        val len = write(buffer)
        if (len <= 0) {
            return len
        }
        written += len
    }
    return written
}

class ServerLauncherSocketProxy(
    private val socket: ServerSocket
): LauncherSocketProxy() {
    override suspend fun start() {
        withContext(Dispatchers.IO) {
            val clients = ArrayList<Socket>()
            val clientLock = Mutex()
            logger.info("Server send started")
            launch {
                sendQueue.collect { message ->
                    val buffer = ByteBuffer.allocate(1024)
                    message.encode(buffer)
                    buffer.flip()
                    val len = buffer.limit()
                    logger.trace("Send $len bytes to ${clients.size} client")
                    for (client in clients) {
                        runInterruptible { client.write(buffer) }
                        buffer.clear()
                        buffer.limit(len)
                    }
                    logger.trace("Sent $len bytes to ${clients.size} client")
                }
            }
            while (true) {
                val client = try {
                    runInterruptible { socket.accept() }
                } catch (ex: ClosedByInterruptException) {
                    throw kotlin.coroutines.cancellation.CancellationException()
                }
                logger.trace("Client connected")
                launch {
                    clientLock.withLock {
                        if (clients.isEmpty()) {
                            _connected.value = true
                        }
                        clients.add(client)
                    }
                    logger.info("Server receive started")
                    receiveChannel(client::read)
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
