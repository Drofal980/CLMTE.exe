package com.clmte_exe.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun Taskbar(
    onStartClick: () -> Unit,
    onClockClick: () -> Unit
) {
    val time = rememberTime()

    // The Box provides the background that extends behind the navigation bar
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFBFBFBF))
            .navigationBarsPadding() // This pushes the Row above the navigation bar
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .clickable { onStartClick() }
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.start_button),
                    contentDescription = "Start",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.height(32.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .clickable { onClockClick() }
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = time,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun rememberTime(): String {
    val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return formatter.format(Date())
}
