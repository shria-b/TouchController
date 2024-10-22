package top.fifthlight.touchcontroller.layout

import top.fifthlight.touchcontroller.control.ControllerWidget
import top.fifthlight.touchcontroller.model.CrosshairStateModel
import top.fifthlight.touchcontroller.proxy.data.Offset

fun Context.Hud(widgets: List<ControllerWidget>, crosshairModel: CrosshairStateModel, onViewDelta: (Offset) -> Unit) {
    widgets.forEach { widget ->
        withAlign(
            align = widget.align,
            offset = widget.offset,
            size = widget.size()
        ) {
            widget.layout(this)
        }
    }

    View(
        crosshairStatus = crosshairModel.state.status,
        onNewCrosshairStatus = {
            crosshairModel.updateStatus(it)
        },
        onPointerDelta = onViewDelta
    )
    Crosshair(crosshairModel.state)
}