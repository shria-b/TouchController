package top.fifthlight.touchcontroller.proxy.message

import top.fifthlight.touchcontroller.proxy.data.Offset
import java.nio.ByteBuffer

data class AddPointerMessage(
    val index: Int,
    val position: Offset
): ProxyMessage() {
    override val type: Int = 1

    override fun encode(buffer: ByteBuffer) {
        buffer.putInt(index)
        buffer.putFloat(position.x)
        buffer.putFloat(position.y)
    }

    private class Decoder: ProxyMessageDecoder() {
        override fun decode(payload: ByteArray): AddPointerMessage {
            if (payload.size != 8) {
                throw BadMessageLengthException(
                    expected = 8,
                    actual = payload.size
                )
            }
            val buffer = ByteBuffer.wrap(payload)
            return AddPointerMessage(
                index = buffer.getInt(),
                position = Offset(
                    x = buffer.getFloat(),
                    y = buffer.getFloat()
                )
            )
        }
    }

    companion object {
        val DECODER: ProxyMessageDecoder = Decoder()
    }
}