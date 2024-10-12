package top.fifthlight.touchcontroller.layout

import net.minecraft.client.gui.DrawContext
import org.koin.core.component.KoinComponent
import top.fifthlight.touchcontroller.ext.withTranslate
import top.fifthlight.touchcontroller.proxy.data.IntOffset
import top.fifthlight.touchcontroller.proxy.data.IntRect
import top.fifthlight.touchcontroller.proxy.data.IntSize
import top.fifthlight.touchcontroller.proxy.data.Offset
import top.fifthlight.touchcontroller.state.Pointer

data class ContextStatus(
    var forward: Float = 0f,
    var left: Float = 0f,
    var sneak: Boolean = false,
)

data class Context(
    val drawContext: DrawContext,
    val size: IntSize,
    val screenOffset: IntOffset,
    val scale: Float,
    val pointers: Map<Int, Pointer>,
    val status: ContextStatus = ContextStatus()
) : KoinComponent {
    inline fun <reified T> withOffset(offset: IntOffset, crossinline block: Context.() -> T): T =
        drawContext.withTranslate(offset.x.toFloat(), offset.y.toFloat()) {
            copy(
                screenOffset = offset + offset,
                size = size - offset
            ).block()
        }

    inline fun <reified T> withOffset(x: Int, y: Int, crossinline block: Context.() -> T): T =
        withOffset(IntOffset(x, y), block)

    inline fun <reified T> withSize(size: IntSize, crossinline block: Context.() -> T): T = copy(size = size).block()

    inline fun <reified T> withRect(x: Int, y: Int, width: Int, height: Int, crossinline block: Context.() -> T): T =
        drawContext.withTranslate(x.toFloat(), y.toFloat()) {
            copy(
                screenOffset = screenOffset + IntOffset(x, y),
                size = IntSize(width, height)
            ).block()
        }

    inline fun <reified T> withRect(offset: IntOffset, size: IntSize, crossinline block: Context.() -> T): T =
        drawContext.withTranslate(offset.x.toFloat(), offset.y.toFloat()) {
            copy(
                screenOffset = screenOffset + offset,
                size = size
            ).block()
        }

    inline fun <reified T> withRect(rect: IntRect, crossinline block: Context.() -> T): T =
        withRect(rect.offset, rect.size, block)

    val Pointer.scaledOffset: Offset
        get() = ((position / scale) - screenOffset)

    fun Pointer.inRect(size: IntSize): Boolean = scaledOffset in size

    fun getPointersInRect(size: IntSize): List<Pointer> = pointers.values.filter { it.inRect(size) }
}