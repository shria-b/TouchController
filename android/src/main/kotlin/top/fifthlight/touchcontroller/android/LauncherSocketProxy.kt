package top.fifthlight.touchcontroller.android

import android.net.LocalServerSocket
import android.net.LocalSocket
import android.system.Os
import android.system.OsConstants
import top.fifthlight.touchcontroller.proxy.LauncherSocketProxy
import top.fifthlight.touchcontroller.proxy.ServerLauncherSocketProxy
import top.fifthlight.touchcontroller.proxy.ServerSocket
import top.fifthlight.touchcontroller.proxy.Socket
import java.io.File
import java.nio.ByteBuffer

private class SocketChannelProxy(private val socket: LocalSocket) : Socket {
    private val input = socket.inputStream
    private val output = socket.outputStream

    override fun read(buffer: ByteBuffer): Int {
        val array = buffer.array()
        val pos = buffer.position()
        val len = runCatching { input.read(array, pos, buffer.remaining()) }.getOrNull() ?: 0
        buffer.position(pos + len)
        return len
    }

    override fun write(buffer: ByteBuffer): Int {
        val array = buffer.array()
        val pos = buffer.position()
        val len = buffer.remaining()
        output.write(array, pos, len)
        buffer.position(pos + len)
        return len
    }

    override fun close() = socket.close()
}

private class ServerSocketChannelProxy(path: File) : ServerSocket {
    private val fd = Os.open(path.toString(), 0, OsConstants.O_RDONLY)
    private val socket = LocalServerSocket(fd)
    override fun accept(): Socket = SocketChannelProxy(socket.accept())
    override fun close() {
        socket.close()
        Os.close(fd)
    }
}

fun createServerProxy(path: File): LauncherSocketProxy = ServerLauncherSocketProxy(ServerSocketChannelProxy(path))
