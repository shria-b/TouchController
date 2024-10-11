package top.fifthlight.touchcontroller.ext

import net.minecraft.client.util.math.MatrixStack

inline fun MatrixStack.withMatrix(crossinline block: MatrixStack.() -> Unit) {
    push()
    block()
    pop()
}