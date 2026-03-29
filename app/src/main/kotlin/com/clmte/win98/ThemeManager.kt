package com.clmte_exe.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

object ThemeManager {
    var isDarkMode by mutableStateOf(false)
    var uiFontSizeSp by mutableStateOf(14f)

    val uiFontScale: Float
        get() = (uiFontSizeSp / 14f).coerceIn(0.75f, 1.6f)

    // Desktop wallpaper: teal in light, dark teal in dark mode
    val desktopBackground: Color
        get() = if (isDarkMode) Color(0xFF003040) else Color(0xFF008080)

    // Taskbar
    val taskbarBackground: Color
        get() = if (isDarkMode) Color(0xFF1A1A1A) else Color(0xFFC0C0C0)
}
