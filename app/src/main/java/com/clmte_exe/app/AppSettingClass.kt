package com.clmte_exe.app

// Storing all the variables for the settings app
data class AppSettingClass(
    val backgroundColor: String,
    val fontSize: Float,
    val notificationsEnabled: Boolean,
    val darkMode: Boolean,
    val availableColors: Map<String, String>
)
