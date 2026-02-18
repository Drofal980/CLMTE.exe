package com.clmte_exe.app

import android.content.Context
import org.json.JSONObject

// Handles the JSON to read the settings
object SettingsManager{

    fun loadSettings(context: Context): AppSettingClass {

        // Getting the JSON file from the raw and reading it.
        val inputStream = context.resources.openRawResource(R.raw.settings)
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        val json = JSONObject(jsonString)

        val colorsObject = json.getJSONObject("availableColors")
        val colorMap = mutableMapOf<String, String>()

        colorsObject.keys().forEach { key ->
            colorMap[key] = colorsObject.getString(key)
        }

        return AppSettingClass(
            backgroundColor = json.getString("backgroundColor"),
            fontSize = json.getDouble("fontSize").toFloat(),
            notificationsEnabled = json.getBoolean("notificationsEnabled"),
            darkMode = json.getBoolean("darkMode"),
            availableColors = colorMap
        )

    }
}