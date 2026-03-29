package com.clmte_exe.app.todo

import androidx.compose.ui.graphics.Color
import java.util.UUID

val PastelGreen = Color(0xFFB5EAD7)
val PastelBlue = Color(0xFFC7CEEA)
val PastelPink = Color(0xFFFFDAC1)
val PastelYellow = Color(0xFFFFF2B2)
val PastelPurple = Color(0xFFE2F0CB)
val BackgroundColor = Color(0xFF89ABBB)

val pastelColors = listOf(PastelGreen, PastelBlue, PastelPink, PastelYellow, PastelPurple)

data class TodoItem(
    val id: String = UUID.randomUUID().toString(),
    val carId: String,
    val title: String,
    val description: String,
    val isDone: Boolean = false,
    val color: Color = pastelColors.random(),
    val createdAt: Long = System.currentTimeMillis()
)