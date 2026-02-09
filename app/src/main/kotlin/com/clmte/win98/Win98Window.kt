package com.clmte.win98

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

        val windowWidth = screenWidth * window.widthFraction
        val windowHeight = screenHeight * window.heightFraction

        val density = LocalDensity.current

        if (window.position == Offset.Zero) {
            window.position = with(density) {
                Offset(
                    ((screenWidth - windowWidth) / 2).toPx(),
                    ((screenHeight - windowHeight) / 2).toPx()
                )
            }
        }
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        window.position.x.toInt(),
                        window.position.y.toInt()
                    )
                }
                .width(windowWidth)
                .height(windowHeight)
                .zIndex(window.zIndex)
                .clickable { onFocus() }
        ) {
            Win98WindowContent(
                title = window.title,
                onClose = onClose,
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
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFC0C0C0))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(TITLE_BAR_HEIGHT)
                .background(Color(0xFF000080))
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
                        contentDescription = "close",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.height(18.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.maximize),
                        contentDescription = "close",
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
                .background(Color.White)
                .padding(8.dp)
        ) {
            content()
        }
    }
}



