package com.clmte_exe.app

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DesktopScreen() {
    var openApp by remember { mutableStateOf<AppConfig?>(null) }
    var currentTime by remember { mutableStateOf(SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())) }

    // If a window is open, the back button closes the window instead of the app
    BackHandler(enabled = openApp != null) {
        openApp = null
    }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
            kotlinx.coroutines.delay(1000)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF008080)) // win98_teal
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Desktop Icons
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                items(desktopApps) { app ->
                    DesktopIcon(app) { openApp = app }
                }
            }

            // Taskbar
            Taskbar(currentTime)
        }

        // Window Overlay
        openApp?.let { app ->
            Win98Window(
                title = app.label,
                onClose = { openApp = null }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Welcome to ${app.label}", color = Color.Black)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { openApp = null },
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC0C0C0), contentColor = Color.Black),
                        modifier = Modifier.win98Border()
                    ) {
                        Text("OK")
                    }
                }
            }
        }
    }
}

@Composable
fun DesktopIcon(app: AppConfig, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(100.dp)
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        val icon = app.icon
        if (icon is Int) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = app.label,
                modifier = Modifier.size(64.dp)
            )
        } else if (icon is ImageVector) {
            Image(
                imageVector = icon,
                contentDescription = app.label,
                modifier = Modifier.size(64.dp)
            )
        }
        Text(
            text = app.label,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun Taskbar(currentTime: String) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(Color(0xFFC0C0C0))
            .drawBehind {
                drawLine(
                    color = Color.White,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 1.dp.toPx()
                )
            }
            .padding(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Start Button
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(80.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null, // Disable default ripple
                    onClick = { /* Start Menu */ }
                )
                .win98Border(inset = isPressed)
                .background(Color(0xFFC0C0C0)),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(
                    start = if (isPressed) 6.dp else 4.dp,
                    top = if (isPressed) 2.dp else 0.dp
                )
            ) {
                // Placeholder for Windows logo
                Box(Modifier
                    .size(16.dp)
                    .background(Color.Blue))
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "Start",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Clock Area
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(100.dp)
                .win98Border(inset = true)
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = currentTime,
                color = Color.Black,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun Win98Window(
    title: String,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = modifier
                .background(Color(0xFFC0C0C0))
                .win98Border()
        ) {
            // Title Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .background(Color(0xFF000080)) // Win98 Navy Blue
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Surface(
                    modifier = Modifier
                        .size(18.dp)
                        .clickable { onClose() }
                        .win98Border(),
                    color = Color(0xFFC0C0C0)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        modifier = Modifier.padding(2.dp),
                        tint = Color.Black
                    )
                }
            }
            
            // Window Content
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp)
                    .background(Color.White)
                    .win98Border(inset = true)
            ) {
                content()
            }
        }
    }
}

// Custom Modifier for Win98 style borders
fun Modifier.win98Border(inset: Boolean = false): Modifier = this.drawBehind {
    val width = size.width
    val height = size.height
    val stroke = 1.dp.toPx()
    
    if (!inset) {
        // Raised Border
        // Top/Left White
        drawLine(Color.White, Offset(0f, 0f), Offset(width, 0f), stroke)
        drawLine(Color.White, Offset(0f, 0f), Offset(0f, height), stroke)
        // Bottom/Right Black
        drawLine(Color.Black, Offset(width, 0f), Offset(width, height), stroke)
        drawLine(Color.Black, Offset(0f, height), Offset(width, height), stroke)
    } else {
        // Inset Border
        // Top/Left Gray
        drawLine(Color.Gray, Offset(0f, 0f), Offset(width, 0f), stroke)
        drawLine(Color.Gray, Offset(0f, 0f), Offset(0f, height), stroke)
        // Bottom/Right White
        drawLine(Color.White, Offset(width, 0f), Offset(width, height), stroke)
        drawLine(Color.White, Offset(0f, height), Offset(width, height), stroke)
    }
}
