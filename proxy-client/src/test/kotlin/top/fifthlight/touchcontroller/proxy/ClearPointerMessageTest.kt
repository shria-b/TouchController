package top.fifthlight.touchcontroller.proxy

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test
import top.fifthlight.touchcontroller.proxy.message.ClearPointerMessage
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ClearPointerMessageTest: MessageTest() {
    @Test
    fun test() {
        val (server, client) = createProxyPair()
        try {
            runBlocking {
                launch { server.start() }
                launch { client.start() }

                client.connected.first { it }
                server.connected.first { it }

                server.send(ClearPointerMessage)

                val message: ClearPointerMessage? = null
                client.receive { msg ->
                    assertTrue(msg is ClearPointerMessage)
                    assertNull(message)
                }

                cancel()
            }
        } catch (_: CancellationException) {}
    }
}