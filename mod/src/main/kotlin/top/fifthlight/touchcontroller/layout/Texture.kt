package top.fifthlight.touchcontroller.layout

import net.minecraft.util.Identifier
import top.fifthlight.touchcontroller.ext.drawTexture
import top.fifthlight.touchcontroller.proxy.data.Rect

fun Context.Texture(id: Identifier, textureUv: Rect = Rect.ONE) {
    drawContext.drawTexture(
        id = id,
        dstRect = Rect(size = size.toSize()),
        uvRect = textureUv
    )
}

fun Context.Texture(id: Identifier, textureUv: Rect = Rect.ONE, color: Int) {
    drawContext.drawTexture(
        id = id,
        dstRect = Rect(size = size.toSize()),
        uvRect = textureUv,
        color = color
    )
}