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

                val type = try {
                    CarType.valueOf(vehicle.vehicle_type.uppercase())
                } catch (e: Exception) {
                    CarType.SEDAN
                }
                
                val template = carTemplates[type] ?: carTemplates[CarType.SEDAN]!!

                val car = GarageCar(
                    id = vehicleId,
                    title = vehicle.nickname.ifBlank {
                        "${vehicle.year} ${vehicle.make} ${vehicle.model}"
                    },
                    imageRes = template.imageRes,
                    transmissionInfo = template.transmissionInfo,
                    engineInfo = template.engineInfo,
                    tireInfo = template.tireInfo,
                    suspensionInfo = template.suspensionInfo,
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
                        appendLine()
                        append(template.fullDetails)
                    }
                )
                cars.add(car)
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

                    cars.add(
                        GarageCar(
                            id = vehicle.id,
                            title = vehicle.nickname.ifBlank { "${vehicle.year} ${vehicle.make} ${vehicle.model}" },
                            imageRes = template.imageRes,
                            transmissionInfo = template.transmissionInfo,
                            engineInfo = template.engineInfo,
                            tireInfo = template.tireInfo,
                            suspensionInfo = template.suspensionInfo,
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
                                appendLine()
                                append(template.fullDetails)
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
    TRUCK("Truck")
}

data class CarTemplate(
    val imageRes: Int,
    val transmissionInfo: String,
    val engineInfo: String,
    val tireInfo: String,
    val suspensionInfo: String,
    val fullDetails: String
)

val carTemplates = mapOf(
    CarType.SEDAN to CarTemplate(
        imageRes = R.drawable.sedan,
        transmissionInfo = "TYPE: 5-Speed Manual (MT5)\nGEAR RATIOS: 3.54, 2.13, 1.48, 1.09, 0.89\nFINAL DRIVE: 4.11\nCLUTCH: Hydraulic Single Plate",
        engineInfo = "TYPE: 2.0L Inline-4 DOHC\nHORSEPOWER: 158 hp @ 6500 rpm\nTORQUE: 138 lb-ft @ 4200 rpm\nCOMPRESSION: 10.8:1\nFUEL SYSTEM: Multi-Point Injection",
        tireInfo = "SIZE: 205/55R16\nTYPE: All-Season Radial\nSPEED RATING: H (130 mph)\nPRESSURE: 32 PSI Front / 30 PSI Rear",
        suspensionInfo = "FRONT: Independent MacPherson Strut\nREAR: Multi-Link Independent\nSHOCKS: Gas-Pressurized Twin-Tube",
        fullDetails = "A reliable and comfortable sedan, perfect for daily commuting and road trips."
    ),
    CarType.SUV to CarTemplate(
        imageRes = R.drawable.suv,
        transmissionInfo = "TYPE: Continuously Variable (CVT)\nMODE: Sport Mode with 7 Simulated Steps\nDRIVETRAIN: Real-Time AWD",
        engineInfo = "TYPE: 2.5L Atkinson Cycle Hybrid\nSYSTEM HP: 219 net hp\nELECTRIC MOTOR: 118 hp (Front), 54 hp (Rear)\nBATTERY: 1.6 kWh Li-Ion",
        tireInfo = "SIZE: 225/60R18\nTYPE: Low Rolling Resistance\nRATING: All-Season Touring\nPRESSURE: 35 PSI All Around",
        suspensionInfo = "FRONT: Sport-Tuned Independent Strut\nREAR: Double Wishbone Multi-Link\nGROUND CLEARANCE: 8.4 inches",
        fullDetails = "A versatile SUV that blends rugged utility with hybrid efficiency."
    ),
    CarType.TRUCK to CarTemplate(
        imageRes = R.drawable.truck,
        transmissionInfo = "TYPE: 10-Speed Automatic (SelectShift)\nMODES: Tow/Haul, Snow/Wet, Eco, Sport\nCOOLER: Heavy-Duty Auxiliary Oil Cooler",
        engineInfo = "TYPE: 3.5L Twin-Turbo V6\nHORSEPOWER: 400 hp @ 6000 rpm\nTORQUE: 500 lb-ft @ 3100 rpm\nTOWING CAP: 14,000 lbs",
        tireInfo = "SIZE: LT275/65R18\nTYPE: All-Terrain (A/T)\nLOAD RANGE: E (10-ply)\nPRESSURE: 50 PSI (Heavy Load)",
        suspensionInfo = "FRONT: Independent Double Wishbone\nREAR: Leaf Springs with Outboard Shocks\nAXLE: Electronic Locking Rear Diff",
        fullDetails = "A heavy-duty truck built for the toughest jobs. High-strength steel frame with class-leading towing capacity."
    )
)
