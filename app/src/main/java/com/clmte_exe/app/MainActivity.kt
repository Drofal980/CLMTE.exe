package com.clmte_exe.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.clmte_exe.app.ui.theme.Win98Theme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            // Background color of the app
            var isTealBackground by remember { mutableStateOf(true) }

            Win98Theme (
            ) {
                DesktopScreen(
                    isTealBackground = isTealBackground,
                    onBackgroundChange = { isTealBackground = it}
                )
            }
        }
    }
}
