package com.clmte_exe.sub_apps

import androidx.compose.runtime.Composable
import com.clmte_exe.app.R
import com.clmte_exe.sub_apps.todo.TodoApp

enum class AppType {
    SYSTEM,
    ACCESSORY,
    USER
}



data class Win98App(
    val id: String,
    val title: String,
    val iconRes: Int,
    val type: AppType,
    val content: @Composable () -> Unit
)

val allApps = listOf(

    Win98App(
        id = "mydocuments",
        title = "My Garage",
        iconRes = R.drawable.garage,
        type = AppType.SYSTEM
    ) {
        MyGarageApp()
    },

    Win98App(
        id = "mycomputer",
        title = "My Computer",
        iconRes = R.drawable.my_computer,
        type = AppType.SYSTEM
    ) {
        MyComputerApp()
    },

    Win98App(
        id = "notepad",
        title = "Notepad",
        iconRes = R.drawable.ic_notepad,
        type = AppType.ACCESSORY
    ) {
        NotepadApp()
    },

    Win98App(
        id = "calculator",
        title = "Calculator",
        iconRes = R.drawable.ic_calculator,
        type = AppType.ACCESSORY
    ) {
        CalculatorApp()
    },

    // 🖥 Desktop system apps
    Win98App(
        id = "recyclebin",
        title = "Workshop Bin",
        iconRes = R.drawable.empty_rb,
        type = AppType.SYSTEM
    ) {
        RecycleBinApp()
    },

    Win98App(
        id = "iexplorer",
        title = "AI Assistant",
        iconRes = R.drawable.ai,
        type = AppType.SYSTEM
    ) {
        InternetExplorerApp()
    },

    Win98App(
        id = "settings",
        title = "Settings",
        iconRes = R.drawable.settings,
        type = AppType.SYSTEM
    ) {
        SettingsApp()
    },

    Win98App(
        id = "todo",
        title = "MyWork",
        iconRes = R.drawable.todo,
        type = AppType.SYSTEM
    ) {
        TodoApp()
    },
)
