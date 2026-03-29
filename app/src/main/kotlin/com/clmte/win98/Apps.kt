<<<<<<< Updated upstream:app/src/main/kotlin/com/clmte/win98/Apps.kt
package com.clmte.win98

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SettingsApp() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Settings",
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun NotepadApp() {
    var notepadText by remember {
        mutableStateOf("")
    }
    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Text("Notepad", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        TextField(
            value = notepadText,
            onValueChange = { value->
                notepadText = value
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun CalculatorApp() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Calculator", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        Text("Coming soon…")
    }
}

@Composable
fun MyComputerApp() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text("Frequently Asked Questions (FAQ)", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        Text("Q: What is this app used for?")
        Text("A: This app helps you track car maintenance, receive service reminders, and keep all vehicle records in one place.")
        Text("")
    }
}

@Composable
fun RecycleBinApp() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text("Workshop Bin", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        Text("The Workshop Bin is empty.")
    }
}

@Composable
fun MyDocumentsApp() {
    Column(
        modifier = Modifier
            .height(720.dp)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
    ) {

        Image(
            painter = painterResource(R.drawable.carimage),
            contentDescription = "carImage"
        )

        Text("Vehicle Profile", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        Text("Car name: 2020 Ford Mustang")
        Text("Mileage: 85,000 miles")
        Text("Year of manufacture: 1968")
        Spacer(Modifier.height(12.dp))
        Text("Maintenance Log",fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        Text("Oil change – 10 Jan 2026")
        Text("Brake service – 15 Nov 2025")
        Text("Spark plug replacement – 05 Aug 2025")
        Text("Notes: Used original-spec parts")
        Text("Service Reminders", fontWeight = FontWeight.Bold)
        Text("Oil change every 3,000 miles")
        Text("Carburetor tuning every 6 months")
        Text("Battery check every 3 months")
        Text("Spare Parts Record")
        Text("Part name: Fuel pump")
        Text("Condition: Used")
        Text("Source: Local classic car dealer")
        Text("Cost: \$120")
        Spacer(Modifier.height(12.dp))
        Text("Expense Tracker", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        Text("Total spent this year: \$1,450")
        Text("Repairs: \$900")
        Text("Parts: \$550")
        Text("Document Storage")
        Text("Upload photos of invoices")
        Text("Store old service manuals")
        Text("Keep restoration photos")
        Text("Upload photos of invoices")
    }
}

@Composable
fun InternetExplorerApp() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text("AI Assistant", fontWeight = FontWeight.Bold)
    }
}


=======
package com.clmte_exe.app

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.TextUnit
import com.clmte_exe.app.chatbot.GarageChatBotScreen
import com.clmte_exe.app.mygarage.Win98Black
import com.clmte_exe.app.mygarage.Win98Blue
import com.clmte_exe.app.mygarage.Win98Gray
import com.clmte_exe.app.mygarage.win98Border
import com.clmte_exe.app.mygarage.MyGarageApp as GarageApp
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.res.painterResource

private val appBg: Color get() = if (ThemeManager.isDarkMode) Color(0xFF2D2D2D) else Color(0xFFC0C0C0)
private val appText: Color get() = if (ThemeManager.isDarkMode) Color.White else Color.Black
private val appSectionBg: Color get() = if (ThemeManager.isDarkMode) Color(0xFF3A3A3A) else Color(0xFFD4D0C8)

@Composable
fun SettingsApp() {
    val isDark = ThemeManager.isDarkMode
    var aboutOpen by remember { mutableStateOf(false) }
    // Global font size state: 11-22sp, default 14
    val fontSize = ThemeManager.uiFontSizeSp
    val fs = fontSize.sp

    if (aboutOpen) {
        BackHandler { aboutOpen = false }
        AboutScreen(
            onBack = { aboutOpen = false },
            fontSize = fs
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(appBg)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Settings", color = appText, fontWeight = FontWeight.Bold, fontSize = (fontSize + 4).sp)

        // Appearance Section
        Column(
            modifier = Modifier.fillMaxWidth().background(appSectionBg).padding(12.dp)
        ) {
            Text(text = "Appearance", color = appText, fontWeight = FontWeight.Bold, fontSize = fs)
            Spacer(Modifier.height(12.dp))

            // Dark Mode row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Dark Mode", color = appText, fontWeight = FontWeight.Bold, fontSize = fs)
                    Text(
                        text = if (isDark) "Currently: Dark" else "Currently: Light",
                        color = if (isDark) Color.LightGray else Color.DarkGray,
                        fontSize = (fontSize - 2).sp
                    )
                }
                Switch(
                    checked = isDark,
                    onCheckedChange = { ThemeManager.isDarkMode = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF000080),
                        uncheckedThumbColor = Color(0xFF808080),
                        uncheckedTrackColor = Color(0xFFC0C0C0)
                    )
                )
            }

            Spacer(Modifier.height(12.dp))

            // Font Size row
            Text(text = "Font Size", color = appText, fontWeight = FontWeight.Bold, fontSize = fs)
            Spacer(Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "A", color = appText, fontSize = 11.sp)
                Slider(
                    value = fontSize,
                    onValueChange = { ThemeManager.uiFontSizeSp = it },
                    valueRange = 11f..22f,
                    steps = 10,
                    modifier = Modifier.weight(1f),
                    colors = SliderDefaults.colors(
                        thumbColor = Win98Blue,
                        activeTrackColor = Win98Blue,
                        inactiveTrackColor = if (isDark) Color(0xFF555555) else Color(0xFF999999)
                    )
                )
                Text(text = "A", color = appText, fontSize = 22.sp)
            }
            Text(
                text = "Preview: ${fontSize.toInt()}sp — The quick brown fox",
                color = appText,
                fontSize = fs,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // About Section — tappable row
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(appSectionBg)
                .win98Border(pressed = false)
                .clickable { aboutOpen = true }
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "About", color = appText, fontWeight = FontWeight.Bold, fontSize = fs)
                Text(text = "→", color = appText, fontSize = fs)
            }
            Spacer(Modifier.height(4.dp))
            Text("Version: 1.0.0", color = appText, fontSize = (fontSize - 1).sp)
            Text("clmte.exe", color = appText, fontSize = (fontSize - 1).sp)
        }
    }
}

@Composable
fun AboutScreen(
    onBack: () -> Unit,
    fontSize: TextUnit = 14.sp
) {
    val isDark = ThemeManager.isDarkMode

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(appBg)
    ) {
        // Header bar with back button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
                .background(Win98Blue)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(24.dp)
                    .background(Win98Gray)
                    .win98Border(pressed = false)
                    .clickable { onBack() }
            ) {
                Text(text = "←", fontSize = 14.sp, color = Win98Black, fontWeight = FontWeight.Bold)
            }
            Text(
                text = "About",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = fontSize,
                modifier = Modifier.weight(1f)
            )
        }

        // Empty about page content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(appBg),
            contentAlignment = Alignment.Center
        ) {
            // Empty — future content goes here
        }
    }
}

@Composable
fun NotepadApp() {
    var notepadText by remember { mutableStateOf("") }
    val fieldBg = if (ThemeManager.isDarkMode) Color(0xFF1E1E1E) else Color.White
    val fieldText = if (ThemeManager.isDarkMode) Color.White else Color.Black

    Column(modifier = Modifier.fillMaxSize().background(appBg).padding(8.dp)) {
        Text("Notepad", fontWeight = FontWeight.Bold, color = appText)
        Spacer(Modifier.height(8.dp))
        TextField(
            value = notepadText,
            onValueChange = { notepadText = it },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = fieldBg,
                unfocusedContainerColor = fieldBg,
                focusedTextColor = fieldText,
                unfocusedTextColor = fieldText,
                cursorColor = fieldText,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun CalculatorApp() {
    Column(
        modifier = Modifier.fillMaxSize().background(appBg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Calculator", fontWeight = FontWeight.Bold, color = appText)
        Spacer(Modifier.height(12.dp))
        Text("Coming soon…", color = appText)
    }
}

@Composable
fun MyComputerApp() {
    data class Developer(
        val name: String,
        val role: String,
        val imageRes: Int
    )

    val developers = listOf(
        Developer("Brandon", "Backend Developer", R.drawable.brandon),
        Developer("Emily", "Full-Stack Developer", R.drawable.emily),
        Developer("Austin", "Full-Stack Developer", R.drawable.austin),
        Developer("Dias", "Frontend Developer", R.drawable.dias)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(appBg)
            .padding(16.dp)
    ) {
        Text(
            text = "Developers",
            color = appText,
            fontWeight = FontWeight.Bold,
            fontSize = ThemeManager.uiFontSizeSp.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(developers) { developer ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(appSectionBg)
                        .win98Border(pressed = false)
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = developer.imageRes),
                        contentDescription = developer.name,
                        modifier = Modifier
                            .size(96.dp)
                            .background(Win98Gray)
                            .win98Border(pressed = false)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = developer.name,
                            color = appText,
                            fontWeight = FontWeight.Bold,
                            fontSize = ThemeManager.uiFontSizeSp.sp
                        )

                        Text(
                            text = developer.role,
                            color = if (ThemeManager.isDarkMode) Color.LightGray else Color.DarkGray,
                            fontSize = (ThemeManager.uiFontSizeSp - 2).sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MyGarageApp() {
    GarageApp()
}

@Composable
fun InternetExplorerApp() {
    GarageChatBotScreen()
}

>>>>>>> Stashed changes:app/src/main/java/com/clmte_exe/app/Apps.kt
