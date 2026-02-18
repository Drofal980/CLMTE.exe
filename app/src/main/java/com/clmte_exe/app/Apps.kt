package com.clmte_exe.app

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
import androidx.compose.material3.Button
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.material3.Text
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout


// The main buttons for the settings app
@Composable
fun SettingsApp(
    accountClick: () -> Unit = {},
    securityClick: () -> Unit = {},
    notificationsClick: () -> Unit = {},
    preferencesClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    )
    {
        Spacer(Modifier.height(12.dp))
        SettingsButton("Account Info", accountClick)
        SettingsButton("Security", securityClick)
        SettingsButton("Notifications", notificationsClick)
        SettingsButton("Preferences", preferencesClick)
    }
}

// This is the buttons for the settings app
@Composable
fun SettingsButton(
    text: String,
    onClick: () -> Unit
){
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RectangleShape
    ){
        Text(text)
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


