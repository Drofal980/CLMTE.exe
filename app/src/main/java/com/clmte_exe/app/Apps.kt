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
fun InternetExplorerApp() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text("AI Assistant", fontWeight = FontWeight.Bold)
    }
}
