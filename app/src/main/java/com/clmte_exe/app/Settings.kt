package com.clmte_exe.app

import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// This will be where the settings app is displayed
@Composable
fun SettingsContain(
    settings: AppSettingClass,
    onSettingsChange: (AppSettingClass) -> Unit
) {
    var currentScreen by remember { mutableStateOf("main") }

    when (currentScreen){
        "main" -> SettingsApp (
            accountClick = { currentScreen = "account" },
            securityClick = { currentScreen = "security" },
            notificationsClick = { currentScreen = "notifications" },
            preferencesClick = { currentScreen = "preferences" }
        )

        "account" -> AccountScreen {currentScreen = "main"}
        "security" -> SecurityScreen {currentScreen = "main"}
        "notifications" -> NotificationsScreen {currentScreen = "main"}
        "preferences" -> PreferencesScreen(
            onBackClick = {currentScreen = "main"},
            settings = settings,
            onSettingsChange = onSettingsChange
        )
    }
}

@Composable
fun AccountScreen(
    onBackClick: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        // Going back to the main screen.
        Button( onClick = onBackClick){
            Text("Back")
        }
        Text("Account Info", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))

        Text("App Version: 1.0.0")
        Text("Created by: Austin Hatch, Brandon Van Blarcum, Dias Doktyrbek, Emily McGovern, Evan Khoo")
    }

}

@Composable
fun SecurityScreen(
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        // Going back to the main screen for settings.
        Button( onClick = onBackClick){
            Text("Back")
        }

        Text("Security", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
fun NotificationsScreen(
    onBackClick: () -> Unit
){

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {

        // Going back to the main settings screen.
        Button( onClick = onBackClick){
            Text("Back")
        }

        Text("Notifications", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
fun PreferencesScreen(
    onBackClick: () -> Unit,
    settings: AppSettingClass,
    onSettingsChange: (AppSettingClass) -> Unit,
){

    var fontsize by remember { mutableStateOf(16f) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Button( onClick = onBackClick){
            Text("Back")
        }

        Text("Preferences", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))

        // Dark Mode
        /*Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()

        ) {
            Text("Dark Mode")
            Switch(
                }
            )
        }
*/
        Spacer(Modifier.height(12.dp))

        // Changing the background color
        Text("Background Color")

        // Going through all the colors that are in the JSON and displaying them.
        settings.availableColors.forEach {
            (name, hex) ->
            val colorval = Color(android.graphics.Color.parseColor(hex))

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween){
                Row{
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(colorval)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(name)
                }

                // Radio button for all the colors in the JSON file.
                RadioButton(
                    selected = settings.backgroundColor == name,
                    onClick ={
                        onSettingsChange(
                            settings.copy(backgroundColor = name)
                        )
                    }
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        // Font size change
        Text("Font Size")

        Slider(
            value = settings.fontSize,
            onValueChange = {
                onSettingsChange(
                    settings.copy(fontSize = it)
                )
            },
            valueRange = 12f..32f,
        )

        Spacer(Modifier.height(12.dp))

        // Notifications
        /*Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ){
            Text("Notifications")
            Switch(

            )
        }*/
    }
}