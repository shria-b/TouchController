package top.fifthlight.touchcontroller.android

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import top.fifthlight.touchcontroller.proxy.LauncherSocketProxy
import top.fifthlight.touchcontroller.proxy.data.Offset
import top.fifthlight.touchcontroller.proxy.message.AddPointerMessage
import top.fifthlight.touchcontroller.proxy.message.RemovePointerMessage
import java.io.File

class TouchControllerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): View(context, attrs, defStyleAttr) {
    private var proxy: LauncherSocketProxy? = null
    val socketPath: File

    init {
        val socketDir = context.getDir("socket", Context.MODE_PRIVATE).absolutePath
        socketPath = File("$socketDir/launcher.sock")
        findViewTreeLifecycleOwner()?.let { lifecycleOwner ->
            lifecycleOwner.lifecycleScope.launch {
                val socketProxy = createServerProxy(socketPath)
                proxy = socketProxy
                socketProxy.start()
                proxy = null
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                for (i in 0 until event.pointerCount) {
                    proxy?.trySend(AddPointerMessage(
                        index = event.getPointerId(i),
                        position = Offset(
                            x = event.getX(i),
                            y = event.getY(i)
                        )
                    ))
                }
                true
            }
            MotionEvent.ACTION_UP -> {
                for (i in 0 until event.pointerCount) {
                    proxy?.trySend(RemovePointerMessage(
                        index = event.getPointerId(i)
                    ))
                }
                true
            }
            MotionEvent.ACTION_MOVE -> {
                for (i in 0 until event.pointerCount) {
                    proxy?.trySend(AddPointerMessage(
                        index = event.getPointerId(i),
                        position = Offset(
                            x = event.getX(i),
                            y = event.getY(i)
                        )
                    ))
                }
                true
            }
            else -> super.onTouchEvent(event)
        }
    }
}