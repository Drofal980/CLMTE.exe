package com.clmte_exe.app
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


val desktopApps = allApps.filter { it.type == AppType.SYSTEM }
val accessories = allApps.filter { it.type == AppType.ACCESSORY }


@Composable
fun DesktopScreen(
    settings: AppSettingClass,
    onSettingsChange: (AppSettingClass) -> Unit
) {
    val windowManager = remember { WindowManager() }
    var isStartMenuOpen by remember { mutableStateOf(false) }

    BackHandler(
        enabled = isStartMenuOpen || windowManager.windows.isNotEmpty()
    ) {
        when {
            isStartMenuOpen -> {
                isStartMenuOpen = false
            }

            windowManager.windows.isNotEmpty() -> {
                val topWindow = windowManager.windows.maxByOrNull { it.zIndex }
                if (topWindow != null) {
                    windowManager.close(topWindow)
                }
            }
        }
    }

// color of background = 0xFF008080
    val hex = settings.availableColors[settings.backgroundColor] ?: "#008080"
    val backgroundColor = Color(android.graphics.Color.parseColor(hex))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 42.dp)
            .background(
                color = backgroundColor
            )
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(desktopApps) { app ->
                DesktopIcon(
                    app = DesktopApp(
                        id = app.id,
                        title = app.title,
                        iconRes = app.iconRes
                    )
                ) {
                    windowManager.openOrFocus(app.id, app.title) {
                        app.content(settings, onSettingsChange)
                    }
                }
            }
        }

        windowManager.windows.forEach { window ->
            Win98Window(
                window = window,
                onClose = { windowManager.close(window) },
                onFocus = { windowManager.bringToFront(window) }
            )
        }

        if (isStartMenuOpen) {
            StartMenu(
                accessories = accessories,
                onAppSelected = { app ->
                    isStartMenuOpen = false
                    windowManager.openOrFocus(app.id, app.title) {
                        app.content(settings, onSettingsChange)
                    }
                },
                onDismiss = { isStartMenuOpen = false },
                onShutdown = {
                    isStartMenuOpen = false
                }
            )
        }



        Box(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Taskbar(
                onStartClick = {
                    isStartMenuOpen = !isStartMenuOpen
                },
                onClockClick = {
                    windowManager.openOrFocus(
                        id = "datetime",
                        title = "Date/Time"
                    ) {
                        DateTimeApp()
                    }
                }
            )
        }


    }
}
