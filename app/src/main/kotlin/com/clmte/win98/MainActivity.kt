package com.clmte.win98

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.clmte.win98.ui.theme.Win98Theme
import com.clmte_exe.app.ui.theme.CLMTEexeTheme
import androidx.appcompat.app.AppCompatActivity
import android.util.Log

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, DesktopActivity::class.java)
        startActivity(intent)

        enableEdgeToEdge()
        setContent {
            Win98Theme {
                DesktopScreen()
            }
        }


        }
    }

