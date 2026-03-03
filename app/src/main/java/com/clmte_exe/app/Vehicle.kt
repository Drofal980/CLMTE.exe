package com.clmte_exe.app

data class Vehicle(
    var id: String? = null,
    var model: String? = null,
    var year: Int? = null,
    var bodyClass: String? = null,
    var engineCylinders: String? = null,
    var engineDisplacement: String? = null,
    var driveType: String? = null,
    var transmission: String? = null,
    var vin_num: String? = null,
    var odometer: Long? = null
)