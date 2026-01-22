package com.clmte_exe.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.clmte_exe.app.ui.theme.CLMTEexeTheme
import kotlinx.coroutines.launch

class MyCarActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CLMTEexeTheme {
                val scope = rememberCoroutineScope()
                val scale = remember { Animatable(0f) }

                LaunchedEffect(Unit) {
                    scale.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 300)
                    )
                }

                val closeWithAnimation = {
                    scope.launch {
                        scale.animateTo(
                            targetValue = 0f,
                            animationSpec = tween(durationMillis = 250)
                        )
                        finish()
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Win98Window(
                        title = "My Car",
                        onClose = { closeWithAnimation() },
                        modifier = Modifier
                            .fillMaxWidth(0.98f)
                            .fillMaxHeight(0.85f)
                            .graphicsLayer {
                                scaleX = scale.value
                                scaleY = scale.value
                                alpha = scale.value
                            }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Car Model: 1998 Speedster\n" +
                                       "Status: Online\n" +
                                       "Fuel: 85%\n\n" +
                                       "This is a sample window for the My Car application.",
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Button(
                                onClick = { closeWithAnimation() },
                                shape = RectangleShape,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFC0C0C0),
                                    contentColor = Color.Black
                                ),
                                modifier = Modifier.win98Border()
                            ) {
                                Text("Close")
                            }
                        }
                    }
                }
            }
        }
    }
}
