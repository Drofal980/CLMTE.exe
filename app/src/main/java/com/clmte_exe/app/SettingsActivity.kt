package com.clmte_exe.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clmte_exe.app.ui.theme.CLMTEexeTheme
import kotlinx.coroutines.launch

class SettingsActivity : ComponentActivity() {
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

                var darkMode by remember { mutableStateOf(false) }
                var notificationsEnabled by remember { mutableStateOf(true) }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Win98Window(
                        title = "Settings",
                        onClose = { closeWithAnimation() },
                        modifier = Modifier
                            .fillMaxWidth(0.95f)
                            .fillMaxHeight(0.8f)
                            .graphicsLayer {
                                scaleX = scale.value
                                scaleY = scale.value
                                alpha = scale.value
                            }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "System Settings",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            // Settings Options
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                            ) {
                                Checkbox(
                                    checked = darkMode,
                                    onCheckedChange = { darkMode = it },
                                    colors = CheckboxDefaults.colors(checkmarkColor = Color.White, checkedColor = Color.Black)
                                )
                                Text("Enable High Contrast Mode", color = Color.Black)
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                            ) {
                                Checkbox(
                                    checked = notificationsEnabled,
                                    onCheckedChange = { notificationsEnabled = it },
                                    colors = CheckboxDefaults.colors(checkmarkColor = Color.White, checkedColor = Color.Black)
                                )
                                Text("Allow System Notifications", color = Color.Black)
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(
                                    onClick = { closeWithAnimation() },
                                    shape = RectangleShape,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFC0C0C0),
                                        contentColor = Color.Black
                                    ),
                                    modifier = Modifier.win98Border().padding(horizontal = 4.dp)
                                ) {
                                    Text("Apply")
                                }
                                Button(
                                    onClick = { closeWithAnimation() },
                                    shape = RectangleShape,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFC0C0C0),
                                        contentColor = Color.Black
                                    ),
                                    modifier = Modifier.win98Border().padding(horizontal = 4.dp)
                                ) {
                                    Text("Cancel")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
