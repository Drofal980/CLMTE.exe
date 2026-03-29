package com.clmte_exe.app.mygarage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clmte_exe.app.R
import com.clmte_exe.app.api_calls.FirestoreManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

data class GarageCarEntity(
    val id: String = "",
    val carType: String = "SEDAN",
    val nickname: String = "",
    val modelYear: String = "",
    val notes: String = ""
)

class GarageViewModel : ViewModel() {
    private val firestore = FirestoreManager()
    private val _cars = MutableStateFlow<List<GarageCar>>(emptyList())
    val cars: StateFlow<List<GarageCar>> = _cars.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadCars()
    }
    private fun loadCars() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val entities: List<GarageCarEntity> = firestore.getAllDocuments("cars")
                _cars.value = entities.map { it.toGarageCar() }
            } catch (e: Exception) {
                _cars.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addCar(
        type: CarType,
        nickname: String,
        modelYear: String,
        notes: String
    ) {
        viewModelScope.launch {
            val id = UUID.randomUUID().toString()
            val entity = GarageCarEntity(
                id = id,
                carType = type.name,
                nickname = nickname,
                modelYear = modelYear,
                notes = notes
            )
            firestore.saveDocument("cars", id, entity)
            loadCars()
        }
    }

    fun deleteCar(carId: String) {
        viewModelScope.launch {
            firestore.deleteDocument("cars", carId)
            loadCars()
        }
    }

    private fun GarageCarEntity.toGarageCar(): GarageCar {
        val type = try { CarType.valueOf(carType) } catch (e: Exception) { CarType.SEDAN }
        val template = carTemplates[type]!!
        return GarageCar(
            id = id,
            title = nickname.ifBlank { type.label },
            imageRes = template.imageRes,
            transmissionInfo = template.transmissionInfo,
            engineInfo = template.engineInfo,
            tireInfo = template.tireInfo,
            suspensionInfo = template.suspensionInfo,
            fullDetails = buildString {
                appendLine("Type: ${type.label}")
                if (modelYear.isNotBlank()) appendLine("Model/Year: $modelYear")
                if (notes.isNotBlank()) appendLine("Notes: $notes")
                appendLine()
                append(template.fullDetails)
            }
        )
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
    ),
    CarType.MINIVAN to CarTemplate(
        imageRes = R.drawable.minivan,
        transmissionInfo = "TYPE: 8-Speed Automatic",
        engineInfo = "TYPE: 3.5L V6",
        tireInfo = "SIZE: 235/60R18",
        suspensionInfo = "FRONT: Strut\nREAR: Multi-Link",
        fullDetails = "A practical minivan built for families, cargo space, and comfortable daily driving."
    ),
    CarType.MOTORCYCLE to CarTemplate(
        imageRes = R.drawable.motorcycle,
        transmissionInfo = "TYPE: 6-Speed Manual",
        engineInfo = "TYPE: 649cc Parallel-Twin",
        tireInfo = "FRONT: 120/70ZR17\nREAR: 160/60ZR17",
        suspensionInfo = "FRONT: Telescopic Fork\nREAR: Mono-Shock",
        fullDetails = "A lightweight motorcycle suited for city riding and weekend cruising."
    ),
    CarType.HATCHBACK to CarTemplate(
        imageRes = R.drawable.hatchback,
        transmissionInfo = "TYPE: 6-Speed Manual",
        engineInfo = "TYPE: 649cc Parallel-Twin",
        tireInfo = "FRONT: 120/70ZR17\nREAR: 160/60ZR17",
        suspensionInfo = "FRONT: Telescopic Fork\nREAR: Mono-Shock",
        fullDetails = "A lightweight motorcycle suited for city riding and weekend cruising."
    )

)
