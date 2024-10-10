package top.fifthlight.touchcontroller.proxy.message

abstract class ProxyMessageDecoder {
    abstract fun decode(payload: ByteArray): ProxyMessage
}

abstract class MessageDecodeException(message: String? = null, cause: Throwable? = null) : Exception(message, cause)
class BadMessageTypeException(type: Int) : MessageDecodeException("Bad type: $type")
class BadMessageLengthException(expected: Int, actual: Int) :
    MessageDecodeException("Bad message length: expected $expected bytes, but got $actual bytes")

fun decodeMessage(type: Int, payload: ByteArray): ProxyMessage = when (type) {
    0 -> VersionMessage.DECODER
    1 -> AddPointerMessage.DECODER
    else -> throw BadMessageTypeException(type)
}.decode(payload)
