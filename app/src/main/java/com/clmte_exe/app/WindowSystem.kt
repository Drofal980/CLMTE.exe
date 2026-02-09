package com.clmte_exe.app
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.runtime.Composable

data class WindowState(
    val id: String,
    val title: String,
    val content: @Composable () -> Unit,
    val widthFraction: Float = 0.85f,
    val heightFraction: Float = 0.7f,
    var position: Offset = Offset.Zero,
    var zIndex: Float = 0f
)

class WindowManager {
    private var counter = 0f
    val windows = mutableStateListOf<WindowState>()

    private fun findWindow(id: String): WindowState? {
        return windows.firstOrNull { it.id == id }
    }

    fun close(window: WindowState) {
        windows.remove(window)
    }

    fun bringToFront(window: WindowState) {
        window.zIndex = counter++
    }

    fun openOrFocus(
        id: String,
        title: String,
        widthFraction: Float = 0.85f,
        heightFraction: Float = 0.7f,
        content: @Composable () -> Unit
    ) {
        val existingWindow = findWindow(id)

        if (existingWindow != null) {
            bringToFront(existingWindow)
        } else {
            windows.add(
                WindowState(
                    id = id,
                    title = title,
                    content = content,
                    widthFraction = widthFraction,
                    heightFraction = heightFraction,
                    zIndex = counter++
                )
            )
        }
    }

}
