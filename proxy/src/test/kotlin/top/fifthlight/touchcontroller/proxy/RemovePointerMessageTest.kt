package top.fifthlight.touchcontroller.proxy

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test
import top.fifthlight.touchcontroller.proxy.message.RemovePointerMessage
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class RemovePointerMessageTest: MessageTest() {
    @Test
    fun test() {
        val (server, client) = createProxyPair()
        try {
            runBlocking {
                launch { server.start() }
                launch { client.start() }

                client.connected.first { it }
                server.connected.first { it }

                val index = 0x12345678
                server.send(
                    RemovePointerMessage(
                        index = index,
                    )
                )

                var message: RemovePointerMessage? = null
                while (message == null) {
                    client.receive { msg ->
                        assertTrue(msg is RemovePointerMessage)
                        assertNull(message)
                        message = msg
                    }
                }

                val removePointerMessage = message!!
                assertEquals(index, removePointerMessage.index)
                cancel()
            }
        } catch (_: CancellationException) {}
    }
}