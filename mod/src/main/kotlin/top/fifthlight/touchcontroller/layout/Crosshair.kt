package top.fifthlight.touchcontroller.layout

import com.mojang.blaze3d.platform.GlStateManager
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.BufferRenderer
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.util.Colors
import org.joml.Matrix4f
import top.fifthlight.touchcontroller.ext.withBlend
import top.fifthlight.touchcontroller.ext.withBlendFunction
import top.fifthlight.touchcontroller.ext.withMatrix
import top.fifthlight.touchcontroller.proxy.data.IntOffset
import top.fifthlight.touchcontroller.proxy.data.Offset
import top.fifthlight.touchcontroller.state.CrosshairConfig
import top.fifthlight.touchcontroller.state.CrosshairState
import top.fifthlight.touchcontroller.state.CrosshairStatus
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private const val CROSSHAIR_CIRCLE_PARTS = 24
private const val CROSSHAIR_CIRCLE_ANGLE = 2 * PI.toFloat() / CROSSHAIR_CIRCLE_PARTS

private fun point(angle: Float, radius: Float) = Offset(
    left = cos(angle) * radius,
    top = sin(angle) * radius
)

private fun renderOuter(drawContext: DrawContext, config: CrosshairConfig) {
    val matrix4f: Matrix4f = drawContext.matrices.peek().positionMatrix
    val bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR)

    val innerRadius = config.radius.toFloat()
    val outerRadius = (config.radius + config.outerRadius).toFloat()
    var angle = -PI.toFloat() / 2f
    for (i in 0 until CROSSHAIR_CIRCLE_PARTS) {
        val endAngle = angle + CROSSHAIR_CIRCLE_ANGLE
        val point0 = point(angle, outerRadius)
        val point1 = point(endAngle, outerRadius)
        val point2 = point(angle, innerRadius)
        val point3 = point(endAngle, innerRadius)
        angle = endAngle

        bufferBuilder.vertex(matrix4f, point0.x, point0.y, 0f).color(Colors.WHITE)
        bufferBuilder.vertex(matrix4f, point2.x, point2.y, 0f).color(Colors.WHITE)
        bufferBuilder.vertex(matrix4f, point3.x, point3.y, 0f).color(Colors.WHITE)
        bufferBuilder.vertex(matrix4f, point1.x, point1.y, 0f).color(Colors.WHITE)
    }

    BufferRenderer.drawWithGlobalProgram(bufferBuilder.end())
}

private fun renderInner(drawContext: DrawContext, config: CrosshairConfig, state: CrosshairStatus) {
    val matrix4f: Matrix4f = drawContext.matrices.peek().positionMatrix
    val bufferBuilder =
        Tessellator.getInstance().begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR)
    bufferBuilder.vertex(matrix4f, 0f, 0f, 0f).color(Colors.WHITE)

    var angle = 0f
    for (i in 0..CROSSHAIR_CIRCLE_PARTS) {
        val point = point(angle, config.radius * state.breakPercent)
        angle -= CROSSHAIR_CIRCLE_ANGLE

        bufferBuilder.vertex(matrix4f, point.x, point.y, 0f).color(Colors.WHITE)
    }

    BufferRenderer.drawWithGlobalProgram(bufferBuilder.end())
}
fun Context.Crosshair(state: CrosshairState) {
    val status = state.status
    val config = state.config

    if (status == null) {
        return
    }

    val center = IntOffset(
        left = status.position.left.toInt(),
        top = status.position.top.toInt(),
    )

    drawContext.matrices.withMatrix {
        withBlend {
            withBlendFunction(
                srcFactor = GlStateManager.SrcFactor.ONE_MINUS_DST_COLOR,
                dstFactor = GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR,
                srcAlpha = GlStateManager.SrcFactor.ONE,
                dstAlpha = GlStateManager.DstFactor.ZERO
            ) {
                translate(center.left.toFloat(), center.top.toFloat(), 0f)
                renderOuter(drawContext, state.config)
                if (status.breakPercent > 0f) {
                    renderInner(drawContext, state.config, status)
                }
            }
        }
    }
}