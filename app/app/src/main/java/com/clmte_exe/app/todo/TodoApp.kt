package com.clmte_exe.app.todo

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import com.clmte_exe.app.R
import com.clmte_exe.app.mygarage.CarType
import com.clmte_exe.app.mygarage.GarageCarEntity
import com.clmte_exe.app.mygarage.carTemplates
import kotlin.math.roundToInt
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun TodoApp(todoViewModel: TodoViewModel = viewModel()) {
    val todos by todoViewModel.todos.collectAsState()
    val garageCars by todoViewModel.garageCars.collectAsState()
    val selectedCarId by todoViewModel.selectedCarId.collectAsState()
    val isLoading by todoViewModel.isLoading.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                todoViewModel.refreshGarageCars()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    var showCreateDialog by remember { mutableStateOf(false) }
    var todoToEdit by remember { mutableStateOf<TodoItem?>(null) }

    var draggedTodo by remember { mutableStateOf<TodoItem?>(null) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    var dragInitialPosition by remember { mutableStateOf(Offset.Zero) }
    var isDragging by remember { mutableStateOf(false) }
    var appRootCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (selectedCarId != null) {
                        showCreateDialog = true
                    }
                },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Todo")
            }
        },
        containerColor = BackgroundColor
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .onGloballyPositioned { appRootCoordinates = it }
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                if (garageCars.isNotEmpty()) {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp, start = 12.dp, end = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(garageCars, key = { it.id }) { car ->
                            GarageCarPickerCard(
                                car = car,
                                isSelected = selectedCarId == car.id,
                                onClick = { todoViewModel.selectCar(car.id) }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    placeholder = { Text("Search your tasks...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = PastelBlue
                    )
                )

                if (garageCars.isEmpty() && !isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No cars found. Add a car in My Garage first.",
                            color = Color.DarkGray,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp)
                    ) {
                        val filteredTodos = todos.filter {
                            it.title.contains(searchQuery, ignoreCase = true) ||
                                    it.description.contains(searchQuery, ignoreCase = true)
                        }

                        TodoColumn(
                            title = "TODO",
                            appRootCoordinates = appRootCoordinates,
                            modifier = Modifier.weight(1f),
                            items = filteredTodos.filter { !it.isDone },
                            onItemClick = { todoToEdit = it },
                            onDragStart = { todo, startPos ->
                                draggedTodo = todo
                                dragInitialPosition = startPos
                                isDragging = true
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                dragOffset += dragAmount
                            },
                            onDragEnd = {
                                val appWidth = appRootCoordinates?.size?.width?.toFloat() ?: 0f
                                val appHeight = appRootCoordinates?.size?.height?.toFloat() ?: 0f
                                handleDrop(
                                    currentPosition = dragInitialPosition + dragOffset,
                                    draggedTodo = draggedTodo,
                                    appWidthPx = appWidth,
                                    appHeightPx = appHeight,
                                    onMoveToDone = { id ->
                                        todoViewModel.toggleDone(id)
                                    },
                                    onMoveToTodo = { id ->
                                        todoViewModel.toggleDone(id)
                                    },
                                    onDelete = { id ->
                                        todoViewModel.deleteTodo(id)
                                    }
                                )
                                isDragging = false
                                draggedTodo = null
                                dragOffset = Offset.Zero
                                dragInitialPosition = Offset.Zero
                            }
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        TodoColumn(
                            title = "DONE",
                            modifier = Modifier.weight(1f),
                            items = filteredTodos.filter { it.isDone },
                            onItemClick = { todoToEdit = it },
                            onDragStart = { todo, startPos ->
                                draggedTodo = todo
                                dragInitialPosition = startPos
                                isDragging = true
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                dragOffset += dragAmount
                            },
                            onDragEnd = {
                                val appWidth = appRootCoordinates?.size?.width?.toFloat() ?: 0f
                                val appHeight = appRootCoordinates?.size?.height?.toFloat() ?: 0f
                                handleDrop(
                                    currentPosition = dragInitialPosition + dragOffset,
                                    draggedTodo = draggedTodo,
                                    appWidthPx = appWidth,
                                    appHeightPx = appHeight,
                                    onMoveToDone = { id ->
                                        todoViewModel.toggleDone(id)
                                    },
                                    onMoveToTodo = { id ->
                                        todoViewModel.toggleDone(id)
                                    },
                                    onDelete = { id ->
                                        todoViewModel.deleteTodo(id)
                                    }
                                )
                                isDragging = false
                                draggedTodo = null
                                dragOffset = Offset.Zero
                                dragInitialPosition = Offset.Zero
                            },
                            appRootCoordinates = appRootCoordinates
                        )
                    }
                }
            }

            if (isDragging) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 32.dp)
                        .size(64.dp)
                        .shadow(8.dp, CircleShape)
                        .background(Color(0xFFFF6B6B), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Drop Zone",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            if (isDragging && draggedTodo != null) {
                Box(
                    modifier = Modifier
                        .offset {
                            IntOffset(
                                (dragInitialPosition.x + dragOffset.x).roundToInt(),
                                (dragInitialPosition.y + dragOffset.y).roundToInt()
                            )
                        }
                ) {
                    val density = LocalDensity.current
                    val appWidthPx = appRootCoordinates?.size?.width ?: 0

                    val columnWidth = with(density) { (appWidthPx / 2f).toDp() } - 24.dp

                    Box(modifier = Modifier.width(columnWidth)) {
                        TodoCard(item = draggedTodo!!, onClick = {})
                    }
                }
            }
        }

        if ((showCreateDialog && selectedCarId != null) || todoToEdit != null) {
            TodoManageDialog(
                initialTodo = todoToEdit,
                onDismiss = {
                    showCreateDialog = false
                    todoToEdit = null
                },
                onSave = { title, desc ->
                    if (todoToEdit != null) {
                        todoViewModel.updateTodo(
                            todoToEdit!!.copy(title = title, description = desc)
                        )
                    } else {
                        todoViewModel.addTodo(title, desc)
                    }
                    showCreateDialog = false
                    todoToEdit = null
                },
                onDelete = {
                    if (todoToEdit != null) {
                        todoViewModel.deleteTodo(todoToEdit!!.id)
                    }
                    showCreateDialog = false
                    todoToEdit = null
                }
            )
        }
    }
}

@Composable
private fun GarageCarPickerCard(
    car: GarageCarEntity,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val type = try {
        CarType.valueOf(car.carType)
    } catch (e: Exception) {
        CarType.SEDAN
    }

    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFE6F0FF) else Color.White
        ),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) Color(0xFF2F68C4) else Color(0xFFCCD3DC)
        ),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.width(165.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            val imageRes = carTemplates[type]?.imageRes ?: R.drawable.sedan
            Card(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(78.dp)
            ) {
                Icon(
                    painter = painterResource(id = imageRes),
                    contentDescription = type.label,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(6.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = car.nickname.ifBlank { type.label },
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF1D2939)
            )
            if (car.modelYear.isNotBlank()) {
                Text(
                    text = car.modelYear,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF667085)
                )
            }
        }
    }
}

fun handleDrop(
    currentPosition: Offset,
    draggedTodo: TodoItem?,
    appWidthPx: Float,
    appHeightPx: Float,
    onMoveToDone: (String) -> Unit,
    onMoveToTodo: (String) -> Unit,
    onDelete: (String) -> Unit
) {
    if (draggedTodo == null) return

    if (currentPosition.y > appHeightPx * 0.8f) {
        onDelete(draggedTodo.id)
        return
    }

    if (currentPosition.x > appWidthPx * 0.5f && !draggedTodo.isDone) {
        onMoveToDone(draggedTodo.id)
    }
    else if (currentPosition.x < appWidthPx * 0.5f && draggedTodo.isDone) {
        onMoveToTodo(draggedTodo.id)
    }
}





