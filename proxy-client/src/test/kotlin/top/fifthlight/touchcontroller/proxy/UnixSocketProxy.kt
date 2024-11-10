package top.fifthlight.touchcontroller.proxy

import java.net.StandardProtocolFamily
import java.net.UnixDomainSocketAddress
import java.nio.channels.ServerSocketChannel
import java.nio.file.Path

fun unixSocketServerLauncherSocketProxy(path: Path): LauncherSocketProxy {
    val socketAddress = UnixDomainSocketAddress.of(path)
    val socket = ServerSocketChannel.open(StandardProtocolFamily.UNIX)
    socket.bind(socketAddress)
    return ServerLauncherSocketProxy(ServerSocketChannelProxy(socket))
}