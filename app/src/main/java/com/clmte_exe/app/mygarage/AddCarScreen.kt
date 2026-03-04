package com.clmte_exe.app.mygarage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clmte_exe.app.R

@Composable
fun AddCarScreen(
    onSave: (CarType, String, String, String) -> Unit,
    onCancel: () -> Unit
) {
    var selectedType by remember { mutableStateOf<CarType?>(null) }
    var nickname by remember { mutableStateOf("") }
    var modelYear by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Win98Gray)
            .verticalScroll(rememberScrollState())
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Section: Choose Car Type
        Text(
            text = "Select Car Type:",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Win98Black
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CarType.entries.forEach { type ->
                val isSelected = selectedType == type
                val interactionSource = remember { MutableInteractionSource() }
                val isPressed by interactionSource.collectIsPressedAsState()

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .background(if (isSelected) Win98Blue else Win98Gray)
                        .win98Border(pressed = isPressed || isSelected)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) { selectedType = type }
                        .padding(8.dp)
                ) {
                    Image(
                        painter = painterResource(
                            carTemplates[type]?.imageRes ?: R.drawable.sedan
                        ),
                        contentDescription = type.label,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = type.label,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Win98White else Win98Black,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Preview image if type selected
        if (selectedType != null) {
            Text(
                text = "Preview:",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Win98Black
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color.White)
                    .win98Border(pressed = true)
                    .padding(2.dp)
            ) {
                Image(
                    painter = painterResource(
                        carTemplates[selectedType!!]?.imageRes ?: R.drawable.sedan
                    ),
                    contentDescription = "Preview",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // Form fields
        Text(
            text = "Name / Nickname:",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Win98Black
        )
        Win98TextField(value = nickname, onValueChange = { nickname = it }, placeholder = "e.g. Car 1")

        Text(
            text = "Model / Year (optional):",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Win98Black
        )
        Win98TextField(value = modelYear, onValueChange = { modelYear = it }, placeholder = "e.g. 2020 Ford Mustang")

        Text(
            text = "Notes (optional):",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Win98Black
        )
        Win98TextField(
            value = notes,
            onValueChange = { notes = it },
            placeholder = "Any notes...",
            singleLine = false,
            minHeight = 80.dp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Buttons row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
        ) {
            Win98Button(text = "Cancel", onClick = onCancel)
            Win98Button(
                text = "Save",
                enabled = selectedType != null && nickname.isNotBlank(),
                onClick = {
                    selectedType?.let { type ->
                        onSave(type, nickname, modelYear, notes)
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun Win98TextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    singleLine: Boolean = true,
    minHeight: androidx.compose.ui.unit.Dp = 36.dp
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = minHeight)
            .background(Color.White)
            .win98Border(pressed = true)
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Win98DarkGray, fontSize = 12.sp) },
            singleLine = singleLine,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = Win98Black,
                unfocusedTextColor = Win98Black,
                cursorColor = Win98Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 13.sp),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun Win98Button(
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .defaultMinSize(minWidth = 80.dp, minHeight = 28.dp)
            .background(Win98Gray)
            .win98Border(pressed = isPressed)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (enabled) Win98Black else Win98DarkGray
        )
    }
}
