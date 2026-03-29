package com.clmte.win98

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
<<<<<<< Updated upstream:app/src/main/kotlin/com/clmte/win98/MainActivity.kt
import com.clmte.win98.ui.theme.Win98Theme
=======
import com.clmte_exe.app.ui.theme.Win98Theme
>>>>>>> Stashed changes:app/src/main/java/com/clmte_exe/app/MainActivity.kt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Win98Theme {
                DesktopScreen()
            }
        }
    }
}
