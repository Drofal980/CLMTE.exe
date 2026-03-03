package com.clmte_exe.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.clmte_exe.app.ui.theme.Win98Theme
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val repository = VehicleRepository()

        lifecycleScope.launch {

            // 1️⃣ Create test vehicles
            val vehicle1 = Vehicle(
                id = "testVehicle1",
                model = "Camry",
                year = 2020,
                vin_num = "VIN001"
            )
            val vehicle2 = Vehicle(
                id = "testVehicle2",
                model = "Accord",
                year = 2003,
                vin_num = "VIN002"
            )

            repository.saveVehicle(vehicle1)
            repository.saveVehicle(vehicle2)

            println("✅ Vehicles saved")

            // 2️⃣ Fetch all vehicles
            val allVehicles = repository.getAllVehicles()
            println("📄 All vehicles from Firestore: $allVehicles")

            // 3️⃣ Fetch a single vehicle
            val singleVehicle = repository.getVehicle("testVehicle1")
            println("🔹 Single vehicle fetched: $singleVehicle")

            // 4️⃣ Delete one vehicle
            repository.deleteVehicle("testVehicle2")
            println("🗑️ Deleted vehicle2")

            // 5️⃣ Fetch all vehicles again to confirm delete
            val remainingVehicles = repository.getAllVehicles()
            println("📄 Vehicles remaining after delete: $remainingVehicles")
        }
        setContent {
            Win98Theme {
                DesktopScreen()
            }
        }
    }
}
