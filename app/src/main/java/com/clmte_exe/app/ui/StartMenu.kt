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
import com.clmte_exe.sub_apps.mygarage.win98Border

@Composable
fun StartMenu(
    accessories: List<Win98App>,
    mainItems: List<Win98App>,
    onAppSelected: (Win98App) -> Unit,
    onDismiss: () -> Unit,
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
                .height(IntrinsicSize.Min) // Scale height to fit content
                .background(Win98Gray)
                .win98Border(pressed = false)
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
                    text = "",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    softWrap = false,
                    modifier = Modifier
                        .rotate(270f)
                        .wrapContentSize(unbounded = true)
                        .padding(bottom = 12.dp),
                )
            }

            // Menu items
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                // Main items (My Garage, Settings, MyWork)
                mainItems.forEach { app ->
                    StartMenuItem(
                        text = app.title,
                        iconRes = app.iconRes,
                        onClick = { onAppSelected(app) }
                    )
                }

                if (mainItems.isNotEmpty() && accessories.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .padding(vertical = 4.dp, horizontal = 8.dp)
                            .background(Color.Gray)
                    )
                }

                // Accessories
                accessories.forEach { app ->
                    StartMenuItem(
                        text = app.title,
                        iconRes = app.iconRes,
                        onClick = { onAppSelected(app) }
                    )
                }
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .padding(vertical = 4.dp, horizontal = 8.dp)
                        .background(Color.Gray)
                )
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
            .padding(vertical = 4.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = text, fontSize = 14.sp, color = Win98Black)
    }
}
