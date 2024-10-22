package top.fifthlight.touchcontroller.layout

import net.minecraft.client.gui.DrawContext


typealias DrawCall = (DrawContext) -> Unit

class DrawQueue {
    private val items = mutableListOf<DrawCall>()

    fun enqueue(block: DrawCall) {
        items.add(block)
    }

    fun execute(drawContext: DrawContext) {
        items.forEach { it.invoke(drawContext) }
    }
}