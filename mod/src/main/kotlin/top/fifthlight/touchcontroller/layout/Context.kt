package top.fifthlight.touchcontroller.layout

import net.minecraft.client.gui.DrawContext
import org.koin.core.component.KoinComponent
import top.fifthlight.touchcontroller.proxy.data.IntOffset
import top.fifthlight.touchcontroller.proxy.data.IntSize
import top.fifthlight.touchcontroller.state.Pointer

data class ContextStatus(
    var forward: Boolean = false,
    var backward: Boolean = false,
    var left: Boolean = false,
    var right: Boolean = false,
    var sneak: Boolean = false,
)

data class Context(
    val drawContext: DrawContext,
    val size: IntSize,
    val offset: IntOffset,
    val scale: Float,
    val pointers: Map<Int, Pointer>,
    val status: ContextStatus = ContextStatus()
) : KoinComponent {
    inline fun <reified T> withOffset(offset: IntOffset, crossinline block: Context.() -> T): T =
        withOffset(offset.x, offset.y, block)

    inline fun <reified T> withOffset(x: Int, y: Int, crossinline block: Context.() -> T): T {
        drawContext.matrices.push()
        drawContext.matrices.translate(x.toFloat(), y.toFloat(), 0f)
        val result = copy(offset = offset + IntOffset(x, y)).block()
        drawContext.matrices.pop()
        return result
    }

    inline fun <reified T> withSize(size: IntSize, crossinline block: Context.() -> T): T = copy(size = size).block()

    fun anyPointerInRect(size: IntSize): List<Pointer> = pointers.values.filter {
        ((it.position / scale) - offset) in size
    }
}