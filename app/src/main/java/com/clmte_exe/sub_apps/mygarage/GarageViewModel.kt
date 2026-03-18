package com.clmte_exe.sub_apps.mygarage

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clmte_exe.app.R
import com.clmte_exe.app.api_calls.FirestoreManager
import kotlinx.coroutines.launch
import java.util.UUID

class GarageViewModel : ViewModel() {
    val cars = mutableStateListOf<GarageCar>()
    private val firestoreManager = FirestoreManager()

    // Starts empty — user adds cars via "+"

    fun addCar(
        vehicle: Vehicle
    ) {

        val vehicleId = UUID.randomUUID().toString()

        viewModelScope.launch {
            firestoreManager.saveDocument(
                collection = "vehicles",
                documentId = vehicleId,
                data = vehicle
            )
        }

        val type = CarType.valueOf(vehicle.vehicle_type.uppercase())
        // Existing local UI logic
        val template = carTemplates[type] ?: return

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
            val vehicles = firestoreManager.getAllDocuments<Vehicle>("vehicles")

            cars.clear()

            vehicles.forEach { vehicle ->
                val type = try {
                    CarType.valueOf(vehicle.vehicle_type?.uppercase().orEmpty())
                } catch (e: Exception) {
                    CarType.SEDAN
                }

                val template = carTemplates[type] ?: carTemplates[CarType.SEDAN]!!

                val vehicleId = vehicle.vin_number?.takeIf { it.isNotBlank() } ?: UUID.randomUUID().toString()

                cars.add(
                    GarageCar(
                        id = vehicleId,
                        title = vehicle.nickname.ifBlank { "${vehicle.year} ${vehicle.make} ${vehicle.model}" },
                        imageRes = template.imageRes,
                        transmissionInfo = template.transmissionInfo,
                        engineInfo = template.engineInfo,
                        tireInfo = template.tireInfo,
                        suspensionInfo = template.suspensionInfo,
                        fullDetails = buildString {
                            appendLine("Type: ${vehicle.vehicle_type ?: "Unknown"}")
                            appendLine("Make: ${vehicle.make ?: "Unknown"}")
                            appendLine("Model: ${vehicle.model ?: "Unknown"}")
                            appendLine("Year: ${vehicle.year ?: "Unknown"}")
                            appendLine("VIN: ${vehicle.vin_number ?: "Unknown"}")
                            appendLine("Drive Type: ${vehicle.drive_type ?: "Unknown"}")
                            appendLine("Transmission: ${vehicle.transmission ?: "Unknown"}")
                            appendLine("Odometer: ${vehicle.odometer ?: "Unknown"}")
                            vehicle.notes?.takeIf { it.isNotEmpty() }?.let {
                                appendLine("Notes: ${it.joinToString()}")
                            }
                            appendLine()
                            append(template.fullDetails)
                        }
                    )
                )
            }
        }
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
