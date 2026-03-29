package com.clmte_exe.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import com.clmte_exe.app.mygarage.Win98Gray
import com.clmte_exe.app.mygarage.Win98Blue
import com.clmte_exe.app.mygarage.Win98White
import com.clmte_exe.app.mygarage.Win98DarkGray
import com.clmte_exe.app.mygarage.Win98Black
import kotlin.collections.forEach

data class Car(
    val title: String,
    val imageRes: Int
)

@Composable
fun MyCarsApp() {
    val cars = listOf(
        Car("Sedan", R.drawable.my_car), // Placeholder: using available asset
        Car("Crossover", R.drawable.my_car),
        Car("Pickup", R.drawable.my_car)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        cars.forEach { car ->
            CarCard(car)
        }
    }
}

@Composable
fun CarCard(car: Car) {
    // Window Frame
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Win98Gray)
            .win98Border(pressed = false)
            .padding(3.dp)
    ) {
        // Title Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .background(Win98Blue)
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.my_documents),
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = car.title,
                color = Win98White,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                modifier = Modifier.weight(1f)
            )

            // Window Controls
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                WinControlButton(R.drawable.minimize)
                WinControlButton(R.drawable.maximize)
                WinControlButton(R.drawable.cross)
            }
        }

        Spacer(modifier = Modifier.height(3.dp))

        // Content Area
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp)
        ) {
            // Car Image Frame (Inset)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(Color.White)
                    .win98Border(pressed = true)
                    .padding(2.dp)
            ) {
                Image(
                    painter = painterResource(car.imageRes),
                    contentDescription = car.title,
                    contentScale = ContentScale.Crop, // Crop to fill frame
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)

            ) {
                val buttonModifier = Modifier.weight(1f).height(65.dp)
                CarActionButton("TRANSMISSION", R.drawable.transmission, Color(0xFF0000FF), buttonModifier)
                CarActionButton("ENGINE", R.drawable.engine, Color.DarkGray, buttonModifier)
                CarActionButton("TIRE", R.drawable.tire, Color.Black, buttonModifier)
                CarActionButton("SUSPENSION", R.drawable.suspension, Color(0xFFFFC107), buttonModifier)
            }
        }
    }
}

@Composable
fun WinControlButton(iconRes: Int) {
    Box(
        modifier = Modifier
            .size(16.dp)
            .background(Win98Gray)
            .win98Border(pressed = false),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = null,
            modifier = Modifier.size(10.dp)
        )
    }
}

@Composable
fun CarActionButton(label: String, iconRes: Int, iconTint: Color, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(Win98Gray)
            .win98Border(pressed = false)
            .clickable { /* Handle click */ }
            .padding(4.dp)
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = label,
            colorFilter = ColorFilter.tint(iconTint),
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center,
            lineHeight = 10.sp
        )
    }
}

fun Modifier.win98Border(pressed: Boolean): Modifier = this.drawWithContent {
    drawContent()

    val stroke = 1.dp.toPx()
    val w = size.width
    val h = size.height

    // Top/Left is Light for Raised, Dark for Pressed
    val topLeftColor = if (pressed) Win98Black else Win98White
    // Bottom/Right is Dark for Raised, Light for Pressed
    val bottomRightColor = if (pressed) Win98White else Win98Black

    // Inner shadow colors
    val innerShadowColor = if (pressed) Win98DarkGray else Win98DarkGray
    // Wait, Win98 raised button: Top/Left White, Bottom/Right Black. Inner Bottom/Right DarkGray.
    // Win98 pressed button: Top/Left Black, Bottom/Right White. Inner Top/Left DarkGray.

    val shadowColor = Win98DarkGray

    // Outer Borders
    // Top
    drawLine(topLeftColor, Offset(0f, stroke/2), Offset(w, stroke/2), stroke)
    // Left
    drawLine(topLeftColor, Offset(stroke/2, 0f), Offset(stroke/2, h), stroke)
    // Bottom
    drawLine(bottomRightColor, Offset(0f, h - stroke/2), Offset(w, h - stroke/2), stroke)
    // Right
    drawLine(bottomRightColor, Offset(w - stroke/2, 0f), Offset(w - stroke/2, h), stroke)

    if (!pressed) {
        // Inner Bottom/Right Shadow for Raised
        drawLine(shadowColor, Offset(stroke, h - stroke*1.5f), Offset(w - stroke, h - stroke*1.5f), stroke)
        drawLine(shadowColor, Offset(w - stroke*1.5f, stroke), Offset(w - stroke*1.5f, h - stroke), stroke)
    } else {
        // Inner Top/Left Shadow for Pressed
        drawLine(shadowColor, Offset(stroke, stroke*1.5f), Offset(w - stroke, stroke*1.5f), stroke)
        drawLine(shadowColor, Offset(stroke*1.5f, stroke), Offset(stroke*1.5f, h - stroke), stroke)
    }
}
