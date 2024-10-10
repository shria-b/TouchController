package top.fifthlight.touchcontroller.proxy.message

import java.nio.ByteBuffer

sealed class ProxyMessage {
    abstract val type: Int

    abstract fun encode(buffer: ByteBuffer)
}
