package top.fifthlight.touchcontroller.proxy.message

import java.nio.ByteBuffer

data class VersionMessage(
    val version: String
): ProxyMessage() {
    override val type: Int = 0

    override fun encode(buffer: ByteBuffer) {
        buffer.put(version.encodeToByteArray())
    }

    private class Decoder: ProxyMessageDecoder() {
        override fun decode(payload: ByteArray) = VersionMessage(payload.decodeToString())
    }

    companion object {
        val DECODER: ProxyMessageDecoder = Decoder()
    }
}