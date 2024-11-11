package top.fifthlight.touchcontroller.layout

import top.fifthlight.touchcontroller.control.ControllerWidget

fun Context.Hud(widgets: List<ControllerWidget>) {
    widgets.forEach { widget ->
        withAlign(
            align = widget.align,
            offset = widget.offset,
            size = widget.size()
        ) {
            widget.layout(this)
        }
    }

    if (client.currentScreen == null) {
        View()
        Crosshair()
        if (config.showPointers) {
            Pointers()
        }
    }
}