package com.clmte_exe.app

import androidx.compose.runtime.Composable

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
        title = "Ford Mustang",
        iconRes = R.drawable.my_documents,
        type = AppType.SYSTEM
    ) {
        MyDocumentsApp()
    },

    Win98App(
        id = "mycomputer",
        title = "FAQ",
        iconRes = R.drawable.my_computer,
        type = AppType.SYSTEM
    ) {
        MyComputerApp()
    },

    Win98App(
        id = "notepad",
        title = "Notepad",
        iconRes = R.drawable.ic_launcher_foreground,
        type = AppType.ACCESSORY
    ) {
        NotepadApp()
    },

    Win98App(
        id = "calculator",
        title = "Calculator",
        iconRes = R.drawable.ic_launcher_foreground,
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
)
