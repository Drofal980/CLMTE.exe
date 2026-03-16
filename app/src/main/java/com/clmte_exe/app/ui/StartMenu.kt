package com.clmte_exe.app.ui

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
import androidx.compose.ui.draw.rotate
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.clmte_exe.sub_apps.Win98App
import com.clmte_exe.sub_apps.mygarage.Win98Blue
import com.clmte_exe.sub_apps.mygarage.Win98Gray
import com.clmte_exe.sub_apps.mygarage.Win98Black

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
                .width(240.dp)
                .height(300.dp)
                .background(Win98Gray)
        ) {
            // Left blue strip
            Box(
                modifier = Modifier
                    .width(36.dp)
                    .fillMaxHeight()
                    .background(Win98Blue),
                contentAlignment = Alignment.BottomCenter,
            ) {
                Text(
                    text = "Windows 98",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    softWrap = false,
                    modifier = Modifier
                        .rotate(270f)
                        .wrapContentSize(unbounded = true)
                        .padding(start = 100.dp, bottom = 6.dp),
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
                    StartMenuItem(
                        text = app.title,
                        iconRes = app.iconRes,
                        onClick = { onAppSelected(app) }
                    )
                }
            }
        }
    }
}

@Composable
private fun StartMenuItem(
    text: String,
    iconRes: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 6.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = text, fontSize = 14.sp, color = Win98Black)
    }
}
