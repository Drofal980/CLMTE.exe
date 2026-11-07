package com.clmte_exe.app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.clmte_exe.app.R
import com.clmte_exe.sub_apps.settings.ThemeManager
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun Taskbar(
    onStartClick: () -> Unit,
    onClockClick: () -> Unit
) {
    val time = rememberTime()
    val taskbarBg = ThemeManager.taskbarBackground
    val textColor = if (ThemeManager.isDarkMode) Color.White else Color.Black

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(taskbarBg)
            .navigationBarsPadding()
            .height(48.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
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
                fontWeight = FontWeight.Medium,
                color = textColor
            )
        }

    }
}

@Composable
private fun rememberTime(): String {
    val formatter = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    var time by remember { mutableStateOf(formatter.format(Date())) }

    LaunchedEffect(Unit) {
        while (true) {
            val now = Calendar.getInstance()
            time = formatter.format(now.time)
            // Calculate delay until the start of the next minute to stay accurate
            val secondsUntilNextMinute = 60 - now.get(Calendar.SECOND)
            delay(secondsUntilNextMinute * 1000L)
        }
    }

    return time
}
