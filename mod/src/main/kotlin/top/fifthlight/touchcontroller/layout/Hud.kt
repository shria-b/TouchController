package top.fifthlight.touchcontroller.layout

import top.fifthlight.touchcontroller.config.control.ControllerWidgetConfig
import top.fifthlight.touchcontroller.model.CrosshairStateModel
import top.fifthlight.touchcontroller.proxy.data.Offset

fun Context.Hud(widgets: List<ControllerWidgetConfig>, crosshairModel: CrosshairStateModel, onViewDelta: (Offset) -> Unit) {
    widgets.forEach { widget ->
        withAlign(
            align = widget.align,
            offset = widget.offset,
            size = widget.size()
        ) {
            widget.render(this)
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