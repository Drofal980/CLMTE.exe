package com.clmte_exe.app.obj_classes

import com.clmte_exe.app.api_calls.FirestoreManager

class VehicleRepository(
    private val firestore: FirestoreManager = FirestoreManager()
) {

    suspend fun getAllVehicles(): List<Vehicle> {
        return firestore.getAllDocuments("vehicles")
    }

    suspend fun getVehicle(vehicleId: String): Vehicle? {
        return firestore.getDocument("vehicles", vehicleId)
    }

    suspend fun saveVehicle(vehicle: Vehicle) {
        val id = vehicle.getId() ?: throw IllegalArgumentException("Vehicle must have an id")
        firestore.saveDocument("vehicles", id, vehicle)
    }

    suspend fun deleteVehicle(vehicleId: String) {
        firestore.deleteDocument("vehicles", vehicleId)
    }

    suspend fun saveAllVehicles(vehicles: List<Vehicle>) {
        vehicles.forEach { saveVehicle(it) }
    }
}