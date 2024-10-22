package top.fifthlight.touchcontroller.layout

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.util.Window
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import top.fifthlight.touchcontroller.ext.scaledSize
import top.fifthlight.touchcontroller.ext.size
import top.fifthlight.touchcontroller.ext.withTranslate
import top.fifthlight.touchcontroller.proxy.data.IntOffset
import top.fifthlight.touchcontroller.proxy.data.IntRect
import top.fifthlight.touchcontroller.proxy.data.IntSize
import top.fifthlight.touchcontroller.proxy.data.Offset
import top.fifthlight.touchcontroller.state.Pointer

data class ClickCounter(
    private var counter: Int = 0
) {
    fun add() {
        counter++
    }

    fun active(): Boolean = counter > 0

    fun consume(): Boolean {
        return if (counter > 0) {
            counter--
            true
        } else {
            false
        }
    }
}

data class ContextResult(
    var forward: Float = 0f,
    var left: Float = 0f,
    var jump: Boolean = false,
    val attack: ClickCounter = ClickCounter(),
    val itemUse: ClickCounter = ClickCounter(),
)

data class ContextStatus(
    var dpadLeftForwardShown: Boolean = false,
    var dpadRightForwardShown: Boolean = false,
    var dpadLeftBackwardShown: Boolean = false,
    var dpadRightBackwardShown: Boolean = false,
    var sneakLocked: Boolean = false
)

data class ContextCounter(
    var tick: Int = 0,
    var sneak: Int = 0,
) {
    fun tick() {
        tick++
        if (sneak > 0) {
            sneak--
        }
    }
}

data class Context(
    val drawContext: DrawContext,
    val size: IntSize,
    val screenOffset: IntOffset,
    val scale: Float,
    val pointers: MutableMap<Int, Pointer>,
    val result: ContextResult = ContextResult(),
    val status: ContextStatus = ContextStatus(),
    val timer: ContextCounter = ContextCounter(),
) : KoinComponent {
    val client: MinecraftClient by inject()
    val window: Window
        get() = client.window

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

    val Pointer.rawOffset: Offset
        get() = position * window.size

    val Pointer.scaledOffset: Offset
        get() = position * window.scaledSize - screenOffset

    fun Pointer.inRect(size: IntSize): Boolean = scaledOffset in size

    fun getPointersInRect(size: IntSize): List<Pointer> = pointers.values.filter { it.inRect(size) }
}