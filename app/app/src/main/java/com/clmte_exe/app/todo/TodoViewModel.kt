package com.clmte_exe.app.todo

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clmte_exe.app.api_calls.FirestoreManager
import com.clmte_exe.app.mygarage.GarageCarEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TodoViewModel : ViewModel() {
    private val firestore = FirestoreManager()
    private val carsCollection = FirestoreManager.COLLECTION_CARS
    private val todosCollection = FirestoreManager.COLLECTION_CAR_TODOS

    private val _garageCars = MutableStateFlow<List<GarageCarEntity>>(emptyList())
    val garageCars: StateFlow<List<GarageCarEntity>> = _garageCars.asStateFlow()

    private val _selectedCarId = MutableStateFlow<String?>(null)
    val selectedCarId: StateFlow<String?> = _selectedCarId.asStateFlow()

    private val _todos = MutableStateFlow<List<TodoItem>>(emptyList())
    val todos: StateFlow<List<TodoItem>> = _todos.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        refreshGarageCars()
    }

    fun refreshGarageCars() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val cars: List<GarageCarEntity> = firestore.getAllDocuments(carsCollection)
                _garageCars.value = cars

                val nextSelectedCarId = when {
                    cars.isEmpty() -> null
                    _selectedCarId.value != null && cars.any { it.id == _selectedCarId.value } -> _selectedCarId.value
                    else -> cars.first().id
                }

                _selectedCarId.value = nextSelectedCarId

                if (nextSelectedCarId == null) {
                    _todos.value = emptyList()
                } else {
                    loadTodosForCar(nextSelectedCarId)
                }
            } catch (e: Exception) {
                _garageCars.value = emptyList()
                _selectedCarId.value = null
                _todos.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectCar(carId: String) {
        if (_selectedCarId.value == carId) return
        _selectedCarId.value = carId
        loadTodosForSelectedCar()
    }

    private fun loadTodosForSelectedCar() {
        val carId = _selectedCarId.value ?: run {
            _todos.value = emptyList()
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                loadTodosForCar(carId)
            } catch (e: Exception) {
                _todos.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun loadTodosForCar(carId: String) {
        val entities: List<TodoFirestoreEntity> = firestore.getDocumentsByField(
            collection = todosCollection,
            field = "carId",
            value = carId
        )

        _todos.value = entities
            .sortedByDescending { it.createdAt }
            .map { it.toTodoItem() }
    }

    fun addTodo(title: String, description: String) {
        val carId = _selectedCarId.value ?: return

        viewModelScope.launch {
            val newTodo = TodoItem(
                carId = carId,
                title = title.trim(),
                description = description.trim()
            )

            val entity = newTodo.toFirestoreEntity()
            firestore.saveDocument(todosCollection, newTodo.id, entity)
            loadTodosForCar(carId)
        }
    }

    fun updateTodo(updatedTodo: TodoItem) {
        viewModelScope.launch {
            val normalizedTodo = updatedTodo.copy(
                title = updatedTodo.title.trim(),
                description = updatedTodo.description.trim()
            )
            firestore.saveDocument(todosCollection, normalizedTodo.id, normalizedTodo.toFirestoreEntity())

            val selectedId = _selectedCarId.value
            if (selectedId != null) {
                loadTodosForCar(selectedId)
            }
        }
    }

    fun toggleDone(todoId: String) {
        val todo = _todos.value.firstOrNull { it.id == todoId } ?: return
        val toggled = todo.copy(isDone = !todo.isDone)

        viewModelScope.launch {
            firestore.saveDocument(todosCollection, toggled.id, toggled.toFirestoreEntity())

            val selectedId = _selectedCarId.value
            if (selectedId != null) {
                loadTodosForCar(selectedId)
            }
        }
    }

    fun deleteTodo(todoId: String) {
        viewModelScope.launch {
            firestore.deleteDocument(todosCollection, todoId)

            val selectedId = _selectedCarId.value
            if (selectedId != null) {
                loadTodosForCar(selectedId)
            } else {
                _todos.value = emptyList()
            }
        }
    }
}

data class TodoFirestoreEntity(
    val id: String = "",
    val carId: String = "",
    val title: String = "",
    val description: String = "",
    val isDone: Boolean = false,
    val colorArgb: Int = PastelBlue.toArgb(),
    val createdAt: Long = 0L
)

private fun TodoFirestoreEntity.toTodoItem(): TodoItem {
    return TodoItem(
        id = id,
        carId = carId,
        title = title,
        description = description,
        isDone = isDone,
        color = Color(colorArgb),
        createdAt = createdAt
    )
}

private fun TodoItem.toFirestoreEntity(): TodoFirestoreEntity {
    return TodoFirestoreEntity(
        id = id,
        carId = carId,
        title = title,
        description = description,
        isDone = isDone,
        colorArgb = color.toArgb(),
        createdAt = createdAt
    )
}
