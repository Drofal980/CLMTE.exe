package com.clmte_exe.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.clmte_exe.app.ui.theme.CLMTEexeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CLMTEexeTheme {
                CLMTEexeApp()
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun CLMTEexeApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        val icon = it.icon
                        if (icon is ImageVector) {
                            Icon(icon, contentDescription = it.label)
                        } else if (icon is Int) {
                            Icon(painterResource(id = icon), contentDescription = it.label)
                        }
                    },
                    label = { Text(it.label) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it }
                )
            }
        }
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            val modifier = Modifier.padding(innerPadding)
            when (currentDestination) {
                AppDestinations.HOME -> HomePage(modifier)
                AppDestinations.MY_CAR -> MyCarPage(modifier)
                AppDestinations.SETTINGS -> SettingsPage(modifier)
            }
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: Any,
) {
    HOME("Home", Icons.Default.Home),
    MY_CAR("My Car", R.drawable.ic_car),
    SETTINGS("Settings", Icons.Default.Settings),
}
