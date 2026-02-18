package com.clmte_exe.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Switch
import androidx.compose.material3.Slider
import androidx.compose.foundation.layout.fillMaxWidth

// This will be where the settings app is displayed
@Composable
fun SettingsContain(
    isTealBackground: Boolean,
    onBackgroundChange: (Boolean) -> Unit
) {
    var currentScreen by remember { mutableStateOf("main") }

    when (currentScreen){
        "main" -> SettingsApp (
            accountClick = { currentScreen = "account"},
            securityClick = { currentScreen = "security" },
            notificationsClick = { currentScreen = "notifications" },
            preferencesClick = { currentScreen = "preferences" }
        )

        "account" -> AccountScreen {currentScreen = "main"}
        "security" -> SecurityScreen {currentScreen = "main"}
        "notifications" -> NotificationsScreen {currentScreen = "main"}
        "preferences" -> PreferencesScreen(
            onBackClick = {currentScreen = "main"},
            isTealBackground = isTealBackground,
            onBackgroundChange = onBackgroundChange
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
    isTealBackground: Boolean,
    onBackgroundChange: (Boolean) -> Unit
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

        Switch(
            checked = isTealBackground,
            onCheckedChange = {onBackgroundChange(it)}
        )

        Spacer(Modifier.height(12.dp))

        // Font size change
        Text("Font Size")

        Slider(
            value = fontsize,
            onValueChange = { fontsize = it },
            valueRange = 12f..32f,
        )
    }
}