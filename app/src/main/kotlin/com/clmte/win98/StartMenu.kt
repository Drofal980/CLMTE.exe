package com.clmte.win98

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StartMenu(
    accessories: List<Win98App>,
    onAppSelected: (Win98App) -> Unit,
    onDismiss: () -> Unit,
    onShutdown: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onDismiss() }
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 40.dp)
                .width(240.dp)
                .height(300.dp)
                .background(Color(0xFFC0C0C0))
        ) {
            // Left blue strip
            Box(
                modifier = Modifier
                    .width(36.dp)
                    .fillMaxHeight()
                    .background(Color(0xFF000080)),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(
                    text = "Windows",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Menu items
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                accessories.forEach { app ->
                    StartMenuItem(app.title) {
                        onAppSelected(app)
                    }
                }
            }
        }
    }
}


@Composable
private fun StartMenuItem(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 6.dp, horizontal = 8.dp)
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            color = Color.Black
        )
    }
}
