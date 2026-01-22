package com.clmte_exe.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.clmte_exe.app.ui.theme.CLMTEexeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CLMTEexeTheme {
                // App Launch Sequence
                //Todo: Add App Launch Sequence
                DesktopScreen()
            }
        }
    }
}
