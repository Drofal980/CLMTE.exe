package com.clmte_exe.sub_apps.mygarage

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.clmte_exe.app.R
import com.clmte_exe.app.ThemeManager
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned

data class GarageCar(
    val id: String,
    val title: String,
    val imageRes: Int,
    val transmissionInfo: String,
    val engineInfo: String,
    val tireInfo: String,
    val suspensionInfo: String,
    val fullDetails: String
)

val myGarageCars = emptyList<GarageCar>()

val Win98Gray: Color get() = if (ThemeManager.isDarkMode) Color(0xFF3A3A3A) else Color(0xFFC0C0C0)
val Win98Blue: Color get() = if (ThemeManager.isDarkMode) Color(0xFF1A1A5C) else Color(0xFF000080)
val Win98White: Color get() = if (ThemeManager.isDarkMode) Color(0xFF5A5A5A) else Color.White
val Win98DarkGray: Color get() = if (ThemeManager.isDarkMode) Color(0xFF2A2A2A) else Color(0xFF808080)
val Win98Black: Color get() = if (ThemeManager.isDarkMode) Color(0xFFE0E0E0) else Color(0xFF000000)

enum class GarageNav { LIST, ADD_CAR, CAR_DETAILS, CAR_INFO }

data class CarComponentInfo(val label: String, val info: String, val iconRes: Int)

@Composable
fun MyGarageApp(garageViewModel: GarageViewModel = viewModel()) {

    // This will add the existing cars that are in firebase
    LaunchedEffect(Unit) {
        garageViewModel.loadCars()
    }

    var currentNav by remember { mutableStateOf(GarageNav.LIST) }
    var selectedCar by remember { mutableStateOf<GarageCar?>(null) }
    var selectedComponent by remember { mutableStateOf<CarComponentInfo?>(null) }

    var trashPosition by remember { mutableStateOf<Offset?>(null) }

    when (currentNav) {
        GarageNav.LIST -> {
            Box(modifier = Modifier.fillMaxSize().background(Win98Gray)) {
                if (garageViewModel.cars.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "No cars yet.\nTap + to add your first car.",
                            color = Win98Black,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp
                        )
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(garageViewModel.cars) { car ->
                            GarageCarCard(
                                car = car,
                                onCardClick = {
                                    selectedCar = car
                                    currentNav = GarageNav.CAR_DETAILS
                                },
                                onDelete = {
                                    garageViewModel.deleteCar(it)
                                },
                                onDrop = { dropPosition ->
                                    trashPosition?.let { trash ->
                                        val trashCenter = trash + Offset(24f, 24f)
                                        val distance = kotlin.math.hypot(
                                            dropPosition.x - trashCenter.x,
                                            dropPosition.y - trashCenter.y
                                        )
                                        if (distance < 48f) {
                                            garageViewModel.deleteCar(car)
                                        }
                                    }
                                }

                            )
                        }
                        item { Spacer(modifier = Modifier.height(72.dp)) }
                    }
                }

                Box(
                    modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp)
                ) {
                    Win98FabButton(onClick = { currentNav = GarageNav.ADD_CAR })
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(20.dp)
                        .size(48.dp)
                        .background(Win98Gray)
                        .win98Border(false)
                        .onGloballyPositioned { coordinates ->
                            trashPosition = coordinates.localToRoot(Offset.Zero)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.trash),
                        contentDescription = "Trash",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }

        GarageNav.ADD_CAR -> {
            BackHandler { currentNav = GarageNav.LIST }
            AddCarScreen(
                onSave = { vehicle ->
                    garageViewModel.addCar(vehicle)
                    currentNav = GarageNav.LIST
                },
                onCancel = { currentNav = GarageNav.LIST }
            )
        }

        GarageNav.CAR_DETAILS -> {
            BackHandler { currentNav = GarageNav.LIST }
            selectedCar?.let { car ->
                CarDetailsScreen(
                    car = car,
                    onClose = { currentNav = GarageNav.LIST },
                    onComponentClick = { component ->
                        selectedComponent = component
                        currentNav = GarageNav.CAR_INFO
                    }
                )
            }
        }

        GarageNav.CAR_INFO -> {
            BackHandler {
                selectedComponent = null
                currentNav = GarageNav.CAR_DETAILS
            }
            selectedComponent?.let { component ->
                CarInfoScreen(
                    component = component,
                    onBack = {
                        selectedComponent = null
                        currentNav = GarageNav.CAR_DETAILS
                    }
                )
            }
        }
    }
}

@Composable
fun Win98FabButton(onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(48.dp)
            .background(Win98Gray)
            .win98Border(pressed = isPressed)
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
    ) {
        Text(text = "+", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Win98Black)
    }
}

@Composable
fun GarageCarCard(
    car: GarageCar,
    onCardClick: () -> Unit,
    onDelete: (GarageCar) -> Unit,
    onDrop: (Offset) -> Unit
) {
    // to be able to drag and drop
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    // Card position
    val cardPosition = remember { mutableStateOf(Offset.Zero) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .offset { IntOffset(offsetX.toInt(), offsetY.toInt()) }
            .onGloballyPositioned { coordinates ->
                cardPosition.value = coordinates.localToRoot(Offset.Zero)}
            .pointerInput(Unit){
                detectDragGestures (
                    onDragEnd = {
                        // reset card position after grabbed
                        val dropPosition = cardPosition.value + Offset(offsetX, offsetY)
                        onDrop(dropPosition)
                        offsetX = 0f
                        offsetY = 0f
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                    }
                )
            }
            .background(Win98Gray)
            .win98Border(pressed = false)
            .padding(3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .background(Win98Blue)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = car.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(3.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(Color.White)
                .win98Border(pressed = true)
                .clickable(onClick = onCardClick)
                .padding(2.dp)
        ) {
            Image(
                painter = painterResource(car.imageRes),
                contentDescription = car.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .background(Win98Blue.copy(alpha = 0.7f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(text = "View Details →", color = Color.White, fontSize = 10.sp)
            }
        }
    }
}

@Composable
fun CarDetailsScreen(
    car: GarageCar,
    onClose: () -> Unit,
    onComponentClick: (CarComponentInfo) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().background(Win98Gray)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                // Car image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(Color.White)
                        .win98Border(pressed = true)
                        .padding(2.dp)
                ) {
                    Image(
                        painter = painterResource(car.imageRes),
                        contentDescription = car.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(text = car.title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Win98Black)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = car.fullDetails, fontSize = 13.sp, color = Win98Black, lineHeight = 19.sp)
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Components:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Win98Black)
                Spacer(modifier = Modifier.height(8.dp))

                // 4 action buttons — navigate to info screen on click
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val btnMod = Modifier.weight(1f).height(64.dp)

                    CarActionButton(
                        label = "TRANSMISSION",
                        iconRes = R.drawable.transmission,
                        modifier = btnMod,
                        onClick = {
                            onComponentClick(
                                CarComponentInfo("Transmission", car.transmissionInfo, R.drawable.transmission)
                            )
                        }
                    )
                    CarActionButton(
                        label = "ENGINE",
                        iconRes = R.drawable.engine,
                        modifier = btnMod,
                        onClick = {
                            onComponentClick(
                                CarComponentInfo("Engine", car.engineInfo, R.drawable.engine)
                            )
                        }
                    )
                    CarActionButton(
                        label = "TIRES",
                        iconRes = R.drawable.tire,
                        modifier = btnMod,
                        onClick = {
                            onComponentClick(
                                CarComponentInfo("Tires", car.tireInfo, R.drawable.tire)
                            )
                        }
                    )
                    CarActionButton(
                        label = "SUSPENSION",
                        iconRes = R.drawable.suspension,
                        modifier = btnMod,
                        onClick = {
                            onComponentClick(
                                CarComponentInfo("Suspension", car.suspensionInfo, R.drawable.suspension)
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun CarInfoScreen(
    component: CarComponentInfo,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().background(Win98Gray)
    ) {
        // Header bar with back button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
                .background(Win98Blue)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Back button
            val interactionSource = remember { MutableInteractionSource() }
            val isPressed by interactionSource.collectIsPressedAsState()
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(24.dp)
                    .background(Win98Gray)
                    .win98Border(pressed = isPressed)
                    .clickable(interactionSource = interactionSource, indication = null, onClick = onBack)
            ) {
                Text(text = "←", fontSize = 14.sp, color = Win98Black, fontWeight = FontWeight.Bold)
            }

            Image(
                painter = painterResource(component.iconRes),
                contentDescription = component.label,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = component.label,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                modifier = Modifier.weight(1f)
            )
        }

        // Info content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .win98Border(pressed = true)
                .padding(12.dp)
        ) {
            // Each line of info rendered as a key-value row
            component.info.lines().forEach { line ->
                if (line.isBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                } else {
                    val parts = line.split(":", limit = 2)
                    if (parts.size == 2) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = parts[0].trim() + ":",
                                fontWeight = FontWeight.Bold,
                                color = Win98Black,
                                fontSize = 12.sp,
                                modifier = Modifier.widthIn(min = 100.dp)
                            )
                            Text(
                                text = parts[1].trim(),
                                color = Win98Black,
                                fontSize = 12.sp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        // Divider line
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(Win98DarkGray.copy(alpha = 0.3f))
                        )
                    } else {
                        Text(
                            text = line,
                            color = Win98Black,
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WinControlButton(
    iconRes: Int,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Box(
        modifier = Modifier
            .size(16.dp)
            .background(Win98Gray)
            .win98Border(pressed = isPressed)
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Image(painter = painterResource(iconRes), contentDescription = null, modifier = Modifier.size(10.dp))
    }
}

@Composable
fun CarActionButton(
    label: String,
    iconRes: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .background(Win98Gray)
            .win98Border(pressed = isPressed)
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
            .padding(4.dp)
    ) {
        Image(painter = painterResource(iconRes), contentDescription = label, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold,
            color = Win98Black,
            textAlign = TextAlign.Center,
            lineHeight = 9.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

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
