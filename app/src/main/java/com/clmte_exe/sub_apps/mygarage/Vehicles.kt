package com.clmte_exe.app.mygarage

// Created a data class so it's easy for database structure
data class Vehicle(
    val nickname: String = "",
    val odometer: Int = 0,
    val vehicle_type: String = "",
    val make: String = "",
    val model: String = "",
    val year: Int = 0,
    val vin_number: String = "",
    val drive_type: String = "",
    val transmission: String = "",
    val notes: List<String> = emptyList()
)

data class ServiceLog(
    val date: String = "",
    val description: String = "",
    val mileage: Int = 0
)
