package com.clmte_exe.sub_apps.mygarage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clmte_exe.app.R
import com.clmte_exe.app.api_calls.FirestoreManager
import kotlinx.coroutines.launch
import java.util.UUID

class GarageViewModel : ViewModel() {
    val cars = mutableStateListOf<GarageCar>()

    val vehicles = mutableStateListOf<Vehicle>()
    private val firestoreManager = FirestoreManager()

    var isLoading by mutableStateOf(false)
        private set

    // Starts empty — user adds cars via "+"

    fun addCar(
        vehicle: Vehicle
    ) {
        viewModelScope.launch {
            isLoading = true
            try {
                val vehicleId = UUID.randomUUID().toString()

                // Create a copy of the vehicle with the new ID if the data class supported it,
                // but since it's a val id we pass it to saveDocument.

                firestoreManager.saveDocument(
                    collection = "vehicles",
                    documentId = vehicleId,
                    data = vehicle.copy(id = vehicleId)
                )

                // Refresh the list from the source of truth (Firestore) 
                // to avoid manual synchronization issues and duplicates.
                loadCars()
            } finally {
                isLoading = false
            }
        }
    }

    fun updateOdometer(vehicleId: String, newOdometer: Int) {
        viewModelScope.launch {
            isLoading = true
            try {
                val vehicle = vehicles.find { it.id == vehicleId }
                if (vehicle != null) {
                    val updatedVehicle = vehicle.copy(odometer = newOdometer)
                    firestoreManager.saveDocument(
                        collection = "vehicles",
                        documentId = vehicleId,
                        data = updatedVehicle
                    )
                    loadCars() // Refresh the UI
                }
            } catch (e: Exception) {
                // Handle error
            } finally {
                isLoading = false
            }
        }
    }

    fun addServiceLog(vehicleId: String, serviceLog: ServiceLog) {
        viewModelScope.launch {
            isLoading = true
            try {
                val vehicle = vehicles.find { it.id == vehicleId }
                if (vehicle != null) {
                    val updatedHistory = vehicle.service_history + serviceLog
                    val updatedVehicle = vehicle.copy(service_history = updatedHistory)
                    firestoreManager.saveDocument(
                        collection = "vehicles",
                        documentId = vehicleId,
                        data = updatedVehicle
                    )
                    loadCars() // Refresh the UI
                }
            } catch (e: Exception) {
                // Handle error
            } finally {
                isLoading = false
            }
        }
    }

    fun fixAllErrors(vehicleId: String, serviceNames: List<String>, odometer: Int, date: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                val vehicle = vehicles.find { it.id == vehicleId }
                if (vehicle != null) {
                    val newLogs = serviceNames.map { name ->
                        ServiceLog(date = date, description = name, mileage = odometer)
                    }
                    val updatedHistory = vehicle.service_history + newLogs
                    val updatedVehicle = vehicle.copy(service_history = updatedHistory)
                    firestoreManager.saveDocument(
                        collection = "vehicles",
                        documentId = vehicleId,
                        data = updatedVehicle
                    )
                    loadCars()
                }
            } catch (e: Exception) {
                // Handle error
            } finally {
                isLoading = false
            }
        }
    }

    fun deleteServiceLog(vehicleId: String, serviceLog: ServiceLog) {
        viewModelScope.launch {
            isLoading = true
            try {
                val vehicle = vehicles.find { it.id == vehicleId }
                if (vehicle != null) {
                    val updatedHistory = vehicle.service_history.filter { it != serviceLog }
                    val updatedVehicle = vehicle.copy(service_history = updatedHistory)
                    firestoreManager.saveDocument(
                        collection = "vehicles",
                        documentId = vehicleId,
                        data = updatedVehicle
                    )
                    loadCars() // Refresh the UI
                }
            } catch (e: Exception) {
                // Handle error
            } finally {
                isLoading = false
            }
        }
    }

    fun deleteCar(car: GarageCar) {
        cars.remove(car)

        viewModelScope.launch {
            firestoreManager.deleteDocument(
                collection = "vehicles",
                documentId = car.id
            )
        }
        loadCars()
    }

    // Load in the cars from firebase
    fun loadCars() {
        viewModelScope.launch {
            isLoading = true
            try {
                val vehiclesFromFire = firestoreManager.getAllDocuments<Vehicle>("vehicles")

                cars.clear()
                vehicles.clear()

                vehiclesFromFire.forEach { vehicle ->

                    vehicles.add(vehicle)

                    val type = try {
                        CarType.valueOf(vehicle.vehicle_type.uppercase())
                    } catch (e: Exception) {
                        CarType.SEDAN
                    }

                    val template = carTemplates[type] ?: carTemplates[CarType.SEDAN]!!

                    val errorCount = InspectionManager.getInspectionErrors(vehicle).size
                    val backgroundRes = when {
                        errorCount >= 15 -> R.drawable.rainy
                        errorCount >= 5 -> R.drawable.hazy
                        else -> R.drawable.sunny
                    }

                    cars.add(
                        GarageCar(
                            id = vehicle.id,
                            title = vehicle.nickname.ifBlank { "${vehicle.year} ${vehicle.make} ${vehicle.model}" },
                            imageRes = template.imageRes,
                            backgroundRes = backgroundRes,
                            fullDetails = buildString {
                                appendLine("Type: ${vehicle.vehicle_type}")
                                appendLine("Make: ${vehicle.make}")
                                appendLine("Model: ${vehicle.model}")
                                appendLine("Year: ${vehicle.year}")
                                appendLine("VIN: ${vehicle.vin_number}")
                                appendLine("Drive Type: ${vehicle.drive_type}")
                                appendLine("Transmission: ${vehicle.transmission}")
                                appendLine("Odometer: ${vehicle.odometer}")
                                if (vehicle.notes.isNotEmpty()) {
                                    appendLine("Notes: ${vehicle.notes.joinToString()}")
                                }
                            }
                        )
                    )
                }
            } finally {
                isLoading = false
            }
        }
    }

    fun getVehiclebyid(id:String): Vehicle?{
        return vehicles.find { it.id == id }
    }
}

enum class CarType(val label: String) {
    SEDAN("Sedan"),
    SUV("SUV"),
    TRUCK("Truck"),
    MINIVAN("Minivan"),
    MOTORCYCLE("Motorcycle"),
    HATCHBACK("Hatchback")
}

data class CarTemplate(
    val imageRes: Int,
)

val carTemplates = mapOf(
    CarType.SEDAN to CarTemplate(
        imageRes = R.drawable.sedan,
    ),
    CarType.SUV to CarTemplate(
        imageRes = R.drawable.suv,
    ),
    CarType.TRUCK to CarTemplate(
        imageRes = R.drawable.truck,
    ),
    CarType.MINIVAN to CarTemplate(
        imageRes = R.drawable.minivan,
    ),
    CarType.MOTORCYCLE to CarTemplate(
        imageRes = R.drawable.motorcycle,
    ),
    CarType.HATCHBACK to CarTemplate(
        imageRes = R.drawable.hatchback,
    )
)
