package com.clmte_exe.app.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.sp
import com.clmte_exe.app.R
import com.clmte_exe.sub_apps.settings.ThemeManager
import androidx.compose.ui.draw.drawWithContent

val Win98Gray: Color get() = if (ThemeManager.isDarkMode) Color(0xFF3A3A3A) else Color(0xFFC0C0C0)
val Win98Blue: Color get() = if (ThemeManager.isDarkMode) Color(0xFF1A1A5C) else Color(0xFF000080)
val Win98White: Color get() = if (ThemeManager.isDarkMode) Color(0xFF5A5A5A) else Color.White
val Win98DarkGray: Color get() = if (ThemeManager.isDarkMode) Color(0xFF2A2A2A) else Color(0xFF808080)
val Win98Black: Color get() = if (ThemeManager.isDarkMode) Color(0xFFE0E0E0) else Color(0xFF000000)

fun Modifier.win98Border(pressed: Boolean): Modifier = this.drawWithContent {
    drawContent()
    val stroke = 1.dp.toPx()
    val w = size.width
    val h = size.height
    val topLeftColor = if (pressed) Win98Black else Win98White
    val bottomRightColor = if (pressed) Win98White else Win98Black
    val shadowColor = Win98DarkGray
    drawLine(topLeftColor, Offset(0f, stroke / 2), Offset(w, stroke / 2), stroke)
    drawLine(topLeftColor, Offset(stroke / 2, 0f), Offset(stroke / 2, h), stroke)
    drawLine(bottomRightColor, Offset(0f, h - stroke / 2), Offset(w, h - stroke / 2), stroke)
    drawLine(bottomRightColor, Offset(w - stroke / 2, 0f), Offset(w - stroke / 2, h), stroke)
    if (!pressed) {
        drawLine(shadowColor, Offset(stroke, h - stroke * 1.5f), Offset(w - stroke, h - stroke * 1.5f), stroke)
        drawLine(shadowColor, Offset(w - stroke * 1.5f, stroke), Offset(w - stroke * 1.5f, h - stroke), stroke)
    } else {
        drawLine(shadowColor, Offset(stroke, stroke * 1.5f), Offset(w - stroke, stroke * 1.5f), stroke)
        drawLine(shadowColor, Offset(stroke * 1.5f, stroke), Offset(stroke * 1.5f, h - stroke), stroke)
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun Win98Window(
    window: WindowState,
    onClose: () -> Unit,
    onFocus: () -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val screenWidth = maxWidth
        val screenHeight = maxHeight

        val windowWidth = if (window.isMaximized) screenWidth else screenWidth * window.widthFraction
        val windowHeight = if (window.isMaximized) screenHeight else screenHeight * window.heightFraction

        val density = LocalDensity.current

        if (window.position == Offset.Zero && !window.isMaximized) {
            window.position = with(density) {
                Offset(
                    ((screenWidth - windowWidth) / 2).toPx(),
                    ((screenHeight - windowHeight) / 2).toPx()
                )
            }
        }

        val offset = if (window.isMaximized) {
            IntOffset.Zero
        } else {
            IntOffset(
                window.position.x.toInt(),
                window.position.y.toInt()
            )
        }

        Box(
            modifier = Modifier
                .offset { offset }
                .width(windowWidth)
                .height(windowHeight)
                .zIndex(window.zIndex)
                .clickable { onFocus() }
        ) {
            Win98WindowContent(
                title = window.title,
                onClose = onClose,
                onMaximize = {
                    onFocus()
                    window.isMaximized = !window.isMaximized
                },
                content = window.content
            )
        }
    }
}

private val TITLE_BAR_HEIGHT = 28.dp


@Composable
fun Win98WindowContent(
    title: String,
    onClose: () -> Unit,
    onMaximize: () -> Unit,
    content: @Composable () -> Unit
) {
    val windowBg = Win98Gray
    val titleBarBg = Win98Blue

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(windowBg)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(28.dp)
                .background(titleBarBg)
                .padding(horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            Row {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.minimize),
                        contentDescription = "minimize",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.height(18.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .clickable { onMaximize() }
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.maximize),
                        contentDescription = "maximize",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.height(18.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .clickable { onClose() }
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.cross),
                        contentDescription = "close",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.height(18.dp)
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(windowBg)
        ) {
            content()
        }
    }
}
