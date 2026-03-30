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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import com.clmte_exe.sub_apps.settings.ThemeManager
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import com.clmte_exe.app.obj_classes.MaintenanceRules
import com.clmte_exe.sub_apps.mygarage.InspectionManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

data class GarageCar(
    val id: String,
    val title: String,
    val imageRes: Int,
    val fullDetails: String
)

val Win98Gray: Color get() = if (ThemeManager.isDarkMode) Color(0xFF3A3A3A) else Color(0xFFC0C0C0)
val Win98Blue: Color get() = if (ThemeManager.isDarkMode) Color(0xFF1A1A5C) else Color(0xFF000080)
val Win98White: Color get() = if (ThemeManager.isDarkMode) Color(0xFF5A5A5A) else Color.White
val Win98DarkGray: Color get() = if (ThemeManager.isDarkMode) Color(0xFF2A2A2A) else Color(0xFF808080)
val Win98Black: Color get() = if (ThemeManager.isDarkMode) Color(0xFFE0E0E0) else Color(0xFF000000)

enum class GarageNav { LIST, ADD_CHOICE, VIN_LOOKUP, ADD_CAR, CAR_DETAILS, CAR_INFO, UPDATE_ODOMETER, ADD_SERVICE_HISTORY, SERVICE_HISTORY }

data class CarComponentInfo(val label: String, val iconRes: Int)

@Composable
fun MyGarageApp(garageViewModel: GarageViewModel = viewModel()) {

    LaunchedEffect(Unit) {
        garageViewModel.loadCars()
    }

    var currentNav by remember { mutableStateOf(GarageNav.LIST) }
    var selectedCarId by remember { mutableStateOf<String?>(null) }
    var selectedComponent by remember { mutableStateOf<CarComponentInfo?>(null) }
    var vehicleToEdit by remember { mutableStateOf<Vehicle?>(null) }
    var predefinedServiceType by remember { mutableStateOf<String?>(null) }

    var trashPosition by remember { mutableStateOf<Offset?>(null) }
    var isDragging by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var carToDelete by remember { mutableStateOf<GarageCar?>(null) }

    if (showDeleteConfirm && carToDelete != null) {
        DeleteConfirm(
            car = carToDelete!!,
            onConfirm = {
                garageViewModel.deleteCar(carToDelete!!)
                showDeleteConfirm = false
                carToDelete = null
            },
            onDismiss = {
                showDeleteConfirm = false
                carToDelete = null
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (currentNav) {
            GarageNav.LIST -> {
                Box(modifier = Modifier.fillMaxSize().background(Win98Gray)) {
                    if (garageViewModel.cars.isEmpty() && !garageViewModel.isLoading) {
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
                                        selectedCarId = car.id
                                        currentNav = GarageNav.CAR_DETAILS
                                    },
                                    onDelete = {
                                        carToDelete = it
                                        showDeleteConfirm = true
                                    },
                                    onDrop = { dropPosition ->
                                        trashPosition?.let { trash ->
                                            val trashCenter = trash + Offset(24f, 24f)
                                            val distance = kotlin.math.hypot(
                                                dropPosition.x - trashCenter.x,
                                                dropPosition.y - trashCenter.y
                                            )
                                            if (distance < 500f) {
                                                carToDelete = car
                                                showDeleteConfirm = true
                                            }
                                        }
                                    },
                                    onDragStart = { isDragging = true },
                                    onDragEnd = { isDragging = false }
                                )
                            }
                            item { Spacer(modifier = Modifier.height(72.dp)) }
                        }
                    }

                    Box(
                        modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp)
                    ) {
                        Win98FabButton(onClick = { currentNav = GarageNav.ADD_CHOICE })
                    }

                    if (isDragging) {
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
            }

            GarageNav.ADD_CHOICE -> {
                BackHandler { currentNav = GarageNav.LIST }
                Column(
                    modifier = Modifier.fillMaxSize().background(Win98Gray).padding(20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Add New Car",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Win98Black
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Win98Button(
                        text = "VIN Lookup",
                        onClick = { currentNav = GarageNav.VIN_LOOKUP }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Win98Button(
                        text = "Manual Setup",
                        onClick = {
                            vehicleToEdit = null
                            currentNav = GarageNav.ADD_CAR
                        }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Win98Button(
                        text = "Cancel",
                        onClick = { currentNav = GarageNav.LIST }
                    )
                }
            }

            GarageNav.VIN_LOOKUP -> {
                BackHandler { currentNav = GarageNav.ADD_CHOICE }
                VIMLookupScreen(
                    onVehicleFound = { vehicle ->
                        vehicleToEdit = vehicle
                        currentNav = GarageNav.ADD_CAR
                    },
                    onCancel = { currentNav = GarageNav.ADD_CHOICE }
                )
            }

            GarageNav.ADD_CAR -> {
                BackHandler { currentNav = GarageNav.ADD_CHOICE }
                AddCarScreen(
                    initialVehicle = vehicleToEdit,
                    onSave = { vehicle ->
                        garageViewModel.addCar(vehicle)
                        currentNav = GarageNav.LIST
                    },
                    onCancel = { currentNav = GarageNav.ADD_CHOICE }
                )
            }

            GarageNav.CAR_DETAILS -> {
                BackHandler { currentNav = GarageNav.LIST }

                selectedCarId?.let { id ->
                    val car = garageViewModel.cars.find { it.id == id }
                    val vehicle = garageViewModel.getVehiclebyid(id)

                    if (car != null && vehicle != null) {
                        CarDetailsScreen(
                            car = car,
                            vehicle = vehicle,
                            onClose = { currentNav = GarageNav.LIST },
                            onComponentClick = { component ->
                                selectedComponent = component
                                currentNav = GarageNav.CAR_INFO
                            },
                            onUpdateOdometer = {
                                currentNav = GarageNav.UPDATE_ODOMETER
                            },
                            onServiceHistoryClick = {
                                currentNav = GarageNav.SERVICE_HISTORY
                            },
                            onFixError = { errorType ->
                                predefinedServiceType = errorType
                                currentNav = GarageNav.ADD_SERVICE_HISTORY
                            }
                        )
                    }
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

            GarageNav.UPDATE_ODOMETER -> {
                BackHandler { currentNav = GarageNav.CAR_DETAILS }
                selectedCarId?.let { id ->
                    val vehicle = garageViewModel.getVehiclebyid(id)
                    vehicle?.let { v ->
                        UpdateOdometerScreen(
                            vehicle = v,
                            onUpdate = { newOdo ->
                                garageViewModel.updateOdometer(v.id, newOdo)
                                currentNav = GarageNav.CAR_DETAILS
                            },
                            onCancel = { currentNav = GarageNav.CAR_DETAILS }
                        )
                    }
                }
            }

            GarageNav.ADD_SERVICE_HISTORY -> {
                BackHandler {
                    predefinedServiceType = null
                    currentNav = if (predefinedServiceType != null) GarageNav.CAR_DETAILS else GarageNav.SERVICE_HISTORY
                }
                selectedCarId?.let { id ->
                    val vehicle = garageViewModel.getVehiclebyid(id)
                    vehicle?.let { v ->
                        AddServiceHistoryScreen(
                            vehicle = v,
                            initialServiceType = predefinedServiceType,
                            onSave = { serviceLog ->
                                garageViewModel.addServiceLog(v.id, serviceLog)
                                val nextNav = if (predefinedServiceType != null) GarageNav.CAR_DETAILS else GarageNav.SERVICE_HISTORY
                                predefinedServiceType = null
                                currentNav = nextNav
                            },
                            onCancel = {
                                val nextNav = if (predefinedServiceType != null) GarageNav.CAR_DETAILS else GarageNav.SERVICE_HISTORY
                                predefinedServiceType = null
                                currentNav = nextNav
                            }
                        )
                    }
                }
            }

            GarageNav.SERVICE_HISTORY -> {
                BackHandler { currentNav = GarageNav.CAR_DETAILS }
                selectedCarId?.let { id ->
                    val vehicle = garageViewModel.getVehiclebyid(id)
                    vehicle?.let { v ->
                        ServiceHistoryScreen(
                            vehicle = v,
                            onAddLog = {
                                predefinedServiceType = null
                                currentNav = GarageNav.ADD_SERVICE_HISTORY
                            },
                            onDeleteLog = { log -> garageViewModel.deleteServiceLog(v.id, log) },
                            onBack = { currentNav = GarageNav.CAR_DETAILS }
                        )
                    }
                }
            }
        }

        // Global Loading Overlay
        if (garageViewModel.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(enabled = true, onClick = {}) // Block interactions
                    .background(Color.Black.copy(alpha = 0.1f)),
                contentAlignment = Alignment.BottomCenter
            ) {
                Column(
                    modifier = Modifier.padding(bottom = 80.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        color = Win98Blue,
                        strokeWidth = 4.dp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Loading Database...",
                        color = Win98Black,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceHistoryScreen(
    vehicle: Vehicle,
    onAddLog: () -> Unit,
    onDeleteLog: (ServiceLog) -> Unit,
    onBack: () -> Unit
) {
    var logToDelete by remember { mutableStateOf<ServiceLog?>(null) }

    if (logToDelete != null) {
        BasicAlertDialog(onDismissRequest = { logToDelete = null }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .background(Win98Gray)
                    .win98Border(true)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Win98Blue)
                        .padding(4.dp)
                ) {
                    Text("Delete Log Entry?", color = Color.White, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    "Are you sure you want to delete this service record?",
                    color = Win98Black,
                    modifier = Modifier.padding(12.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { logToDelete = null }) {
                        Text("Cancel", color = Win98Black)
                    }
                    TextButton(onClick = {
                        onDeleteLog(logToDelete!!)
                        logToDelete = null
                    }) {
                        Text("Delete", color = Color.Red, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Win98Gray)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(36.dp).background(Win98Blue).padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(24.dp).background(Win98Gray).win98Border(pressed = false).clickable(onClick = onBack)
            ) {
                Image(
                    painter = painterResource(R.drawable.backarrow),
                    contentDescription = "backarrow",
                    modifier = Modifier.size(20.dp),
                )
            }
            Text(text = "Service History: ${vehicle.nickname}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.weight(1f))
            
            // Add Log button
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(24.dp).background(Win98Gray).win98Border(pressed = false).clickable(onClick = onAddLog)
            ) {
                Text("+", fontWeight = FontWeight.Bold, color = Win98Black)
            }
        }

        if (vehicle.service_history.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "No service history found.", color = Win98Black, fontSize = 14.sp)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(vehicle.service_history.reversed()) { log ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Win98White)
                            .win98Border(false)
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = log.description, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Win98Black)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = "Date: ${log.date}", fontSize = 12.sp, color = Win98Black)
                                Text(text = "Mileage: ${log.mileage}", fontSize = 12.sp, color = Win98Black)
                            }
                        }
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        // Delete Button
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(24.dp)
                                .background(Color(0xFFE0E0E0))
                                .win98Border(false)
                                .clickable { logToDelete = log }
                        ) {
                            Text("X", color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UpdateOdometerScreen(
    vehicle: Vehicle,
    onUpdate: (Int) -> Unit,
    onCancel: () -> Unit
) {
    var odoText by remember { mutableStateOf(vehicle.odometer.toString()) }

    Column(
        modifier = Modifier.fillMaxSize().background(Win98Gray).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Update Odometer",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Win98Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Current: ${vehicle.odometer} miles",
            fontSize = 14.sp,
            color = Win98Black
        )
        Spacer(modifier = Modifier.height(16.dp))
        Win98TextField(
            value = odoText,
            onValueChange = { odoText = it },
            placeholder = "Enter new mileage"
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            Win98Button(text = "Cancel", onClick = onCancel)
            Win98Button(
                text = "Update",
                enabled = odoText.toIntOrNull() != null,
                onClick = {
                    odoText.toIntOrNull()?.let { onUpdate(it) }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddServiceHistoryScreen(
    vehicle: Vehicle,
    initialServiceType: String? = null,
    onSave: (ServiceLog) -> Unit,
    onCancel: () -> Unit
) {
    val serviceRules = remember { MaintenanceRules().getRules() }
    var expanded by remember { mutableStateOf(false) }
    
    // Set initial service type if provided (e.g. from "Fix" button)
    var selectedServiceName by remember { 
        mutableStateOf(initialServiceType ?: serviceRules.first().name) 
    }
    
    var mileageText by remember { mutableStateOf(vehicle.odometer.toString()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDateText by remember { mutableStateOf("") }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                        sdf.timeZone = TimeZone.getTimeZone("UTC")
                        selectedDateText = sdf.format(Date(it))
                    }
                    showDatePicker = false
                }) {
                    Text("OK", color = Win98Blue)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel", color = Win98Black)
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Win98Gray).padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Add Service History",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Win98Black
        )

        Column {
            Text(text = "Select Service:", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Win98Black)
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .background(Color.White)
                    .win98Border(pressed = true)
                    .clickable { expanded = true }
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(text = selectedServiceName, fontSize = 13.sp, color = Win98Black)
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth(0.8f).background(Win98Gray).win98Border(false)
                ) {
                    serviceRules.forEach { rule ->
                        DropdownMenuItem(
                            text = { Text(rule.name, color = Win98Black) },
                            onClick = {
                                selectedServiceName = rule.name
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        Column {
            Text(text = "Date:", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Win98Black)
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .background(Color.White)
                    .win98Border(pressed = true)
                    .clickable { showDatePicker = true }
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = selectedDateText.ifBlank { "Select Date" },
                    fontSize = 13.sp,
                    color = if (selectedDateText.isBlank()) Win98DarkGray else Win98Black
                )
            }
        }

        Column {
            Text(text = "Mileage at Service:", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Win98Black)
            Spacer(modifier = Modifier.height(4.dp))
            Win98TextField(value = mileageText, onValueChange = { mileageText = it }, placeholder = "e.g. 50000")
        }

        errorMessage?.let {
            Text(text = it, color = Color.Red, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
        ) {
            Win98Button(text = "Cancel", onClick = onCancel)
            Win98Button(
                text = "Save",
                enabled = selectedDateText.isNotBlank() && mileageText.toIntOrNull() != null,
                onClick = {
                    val mileage = mileageText.toIntOrNull() ?: 0
                    if (mileage > vehicle.odometer) {
                        errorMessage = "Service mileage cannot be greater than current odometer (${vehicle.odometer} miles)."
                    } else {
                        errorMessage = null
                        onSave(
                            ServiceLog(
                                date = selectedDateText,
                                description = selectedServiceName,
                                mileage = mileage
                            )
                        )
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteConfirm(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    car: GarageCar
) {
    BasicAlertDialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .background(Win98Gray)
                .win98Border(true)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Win98Blue)
                    .win98Border(false)
                    .padding(4.dp)
            ) {
                Text(
                    text = "Delete ${car.title}?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDismiss) {
                    Box(modifier = Modifier.background(Win98Gray).win98Border(false).padding(10.dp)) {
                        Text(text = "No", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Win98Black)
                    }
                }
                TextButton(onClick = onConfirm) {
                    Box(modifier = Modifier.background(Win98Gray).win98Border(false).padding(10.dp)) {
                        Text(text = "Yes", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Win98Black)
                    }
                }
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
    onDrop: (Offset) -> Unit,
    onDragStart: () -> Unit = {},
    onDragEnd: () -> Unit = {}
) {
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    val cardPosition = remember { mutableStateOf(Offset.Zero) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .offset { IntOffset(offsetX.toInt(), offsetY.toInt()) }
            .onGloballyPositioned { coordinates ->
                cardPosition.value = coordinates.localToRoot(Offset.Zero)
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { onDragStart() },
                    onDragEnd = {
                        onDragEnd()
                        onDrop(cardPosition.value)
                        offsetX = 0f
                        offsetY = 0f
                    },
                    onDragCancel = {
                        onDragEnd()
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarDetailsScreen(
    car: GarageCar,
    onClose: () -> Unit,
    vehicle: Vehicle,
    onComponentClick: (CarComponentInfo) -> Unit,
    onUpdateOdometer: () -> Unit,
    onServiceHistoryClick: () -> Unit,
    onFixError: (String) -> Unit
) {

    var showErrorDialog by remember { mutableStateOf(false) }
    var resolvedErrors by remember {mutableStateOf(setOf<String>())}

    val errors = remember(vehicle) {
        InspectionManager.getInspectionErrors(vehicle)
    }


    Column(
        modifier = Modifier.fillMaxSize().background(Win98Gray)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) { item{
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Back button
                    Box(
                        modifier = Modifier
                            .size(25.dp)
                            .background(Win98Gray)
                            .win98Border(false)
                            .clickable(onClick = onClose),
                        contentAlignment = Alignment.Center,
                    ) {
                        Image(
                            painter = painterResource(R.drawable.backarrow),
                            contentDescription = "backarrow",
                            modifier = Modifier.size(20.dp),
                        )
                    }
                    // Error Icon (only show if errors exist)
                    if (errors.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .background(Win98Gray)
                                .win98Border(false)
                                .clickable { showErrorDialog = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(R.drawable.error), // <-- add icon
                                contentDescription = "Errors",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
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
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Components:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Win98Black)
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val btnMod = Modifier.weight(1f).height(64.dp)
                    CarActionButton(label = "UPDATE ODOMETER", iconRes = R.drawable.transmission, modifier = btnMod, onClick = onUpdateOdometer)
                    CarActionButton(label = "SERVICE HISTORY", iconRes = R.drawable.engine, modifier = btnMod, onClick = onServiceHistoryClick)
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    if (showErrorDialog) {
        BasicAlertDialog(onDismissRequest = { showErrorDialog = false }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Win98Gray)
                    .win98Border(true)
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Win98Blue)
                        .win98Border(false)
                        .padding(4.dp)
                ) {Text(
                    text = "Inspection Errors",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White
                )}

                // Back button for dialog
                Box(
                    modifier = Modifier
                        .size(25.dp)
                        .background(Win98Gray)
                        .win98Border(false)
                        .clickable { showErrorDialog = false }, // Only closes dialog
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        painter = painterResource(R.drawable.backarrow),
                        contentDescription = "backarrow",
                        modifier = Modifier.size(20.dp),
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (errors.isEmpty()) {
                    Text("No issues found!", color = Win98Black)
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 300.dp)
                            .padding(horizontal = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        errors.forEach { error ->
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.White)
                                        .win98Border(pressed = true)
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = error,
                                        color = Color.Red,
                                        fontSize = 12.sp,
                                        modifier = Modifier.weight(1f)
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    // Direct to adding a service log
                                    Box(
                                        modifier = Modifier
                                            .background(Win98Gray)
                                            .win98Border(pressed = false)
                                            .clickable {
                                                showErrorDialog = false
                                                val serviceName = error.substringBefore(":")
                                                onFixError(serviceName)
                                            }
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            "Fixed It",
                                            color = Win98Black,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CarInfoScreen(component: CarComponentInfo, onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().background(Win98Gray)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(36.dp).background(Win98Blue).padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val interactionSource = remember { MutableInteractionSource() }
            val isPressed by interactionSource.collectIsPressedAsState()
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(24.dp).background(Win98Gray).win98Border(pressed = isPressed).clickable(
                    interactionSource = interactionSource, indication = null, onClick = onBack
                )
            ) {
                Image(
                    painter = painterResource(R.drawable.backarrow),
                    contentDescription = "backarrow",
                    modifier = Modifier.size(20.dp),
                )
            }
            Image(painter = painterResource(component.iconRes), contentDescription = component.label, modifier = Modifier.size(30.dp))
            Text(text = component.label, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.weight(1f))
        }
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
        Text(text = label, fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Win98Black, textAlign = TextAlign.Center, lineHeight = 9.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
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
