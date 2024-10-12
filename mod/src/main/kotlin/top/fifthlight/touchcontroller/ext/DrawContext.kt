package top.fifthlight.touchcontroller.ext

import net.minecraft.client.gui.DrawContext
import top.fifthlight.touchcontroller.proxy.data.Offset

inline fun <reified T> DrawContext.withTranslate(offset: Offset, crossinline block: () -> T): T =
    matrices.withMatrix {
        matrices.translate(offset.x, offset.y, 0f)
        block()
    }

inline fun <reified T> DrawContext.withTranslate(x: Float, y: Float, crossinline block: () -> T): T =
    matrices.withMatrix {
        matrices.translate(x, y, 0f)
        block()
    }