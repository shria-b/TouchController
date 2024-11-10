package top.fifthlight.touchcontroller.proxy

import java.nio.ByteBuffer
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel

class SocketChannelProxy(private val channel: SocketChannel) : Socket {
    override fun read(buffer: ByteBuffer): Int = runCatching { channel.read(buffer) }.getOrNull() ?: 0
    override fun write(buffer: ByteBuffer): Int = runCatching { channel.write(buffer) }.getOrNull() ?: 0
    override fun close() = channel.close()
}

class ServerSocketChannelProxy(private val channel: ServerSocketChannel) : ServerSocket {
    override fun accept(): Socket = SocketChannelProxy(channel.accept())
    override fun close() = channel.close()
}