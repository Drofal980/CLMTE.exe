package com.clmte_exe.app.mygarage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.util.Calendar

val Win98Blue = Color(0xFF000080)
val Win98Gray = Color(0xFFC0C0C0)
val Win98White = Color(0xFFFFFFFF)
val Win98DarkGray = Color(0xFF808080)
val Win98Black = Color(0xFF000000)

@Composable
fun Modifier.win98Border(pressed: Boolean): Modifier = this.drawWithContent {
    drawContent()

    val stroke = 1.dp.toPx()
    val w = size.width
    val h = size.height

    val topLeftColor = if (pressed) Win98Black else Win98White
    val bottomRightColor = if (pressed) Win98White else Win98Black
    val shadowColor = Win98DarkGray

    drawLine(topLeftColor, Offset(0f, stroke / 2f), Offset(w, stroke / 2f), stroke)
    drawLine(topLeftColor, Offset(stroke / 2f, 0f), Offset(stroke / 2f, h), stroke)
    drawLine(bottomRightColor, Offset(0f, h - stroke / 2f), Offset(w, h - stroke / 2f), stroke)
    drawLine(bottomRightColor, Offset(w - stroke / 2f, 0f), Offset(w - stroke / 2f, h), stroke)

    if (!pressed) {
        drawLine(shadowColor, Offset(stroke, h - stroke * 1.5f), Offset(w - stroke, h - stroke * 1.5f), stroke)
        drawLine(shadowColor, Offset(w - stroke * 1.5f, stroke), Offset(w - stroke * 1.5f, h - stroke), stroke)
    } else {
        drawLine(shadowColor, Offset(stroke, stroke * 1.5f), Offset(w - stroke, stroke * 1.5f), stroke)
        drawLine(shadowColor, Offset(stroke * 1.5f, stroke), Offset(stroke * 1.5f, h - stroke), stroke)
    }
}
