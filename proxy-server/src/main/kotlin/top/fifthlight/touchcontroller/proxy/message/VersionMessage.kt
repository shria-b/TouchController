package top.fifthlight.touchcontroller.proxy.message

import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

data class VersionMessage(
    val version: String
): ProxyMessage() {
    override val type: Int = 0

    override fun encode(buffer: ByteBuffer) {
        super.encode(buffer)
        buffer.put(version.encodeToByteArray())
    }

    private class Decoder: ProxyMessageDecoder() {
        override fun decode(payload: ByteBuffer) = VersionMessage(StandardCharsets.UTF_8.decode(payload).toString())
    }

    companion object {
        val DECODER: ProxyMessageDecoder = Decoder()
    }
}