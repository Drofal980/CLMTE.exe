package com.clmte_exe.sub_apps.mygarage

import com.clmte_exe.sub_apps.mygarage.Vehicle


// We can add more errors and other things if needed
// This holds all the error if the car needs to be inspected
object InspectionManager {

    fun getInspectionErrors(car: Vehicle): List<String> {
        val errors = mutableListOf<String>()
        val miles = car.odometer

        // 5k–10k miles
        if (miles >= 5000) {
            errors.add("E001: Oil Change Needed")
            errors.add("E002: Tire Rotation Needed")
            errors.add("E003: Basic Inspection Required")
        }

        // 15k miles
        if (miles >= 15000) {
            errors.add("E010: Cabin Filter Replacement")
            errors.add("E011: Air Filter Check")
        }

        // 30k miles
        if (miles >= 30000) {
            errors.add("E020: Brake Fluid Service")
            errors.add("E021: Transmission Fluid Check")
            errors.add("E022: AWD/4WD Fluid Check")
        }

        // 60k miles
        if (miles >= 60000) {
            errors.add("E030: Coolant Replacement")
            errors.add("E031: Spark Plug Check")
            errors.add("E032: Serpentine Belt Inspection")
        }

        // 90k miles
        if (miles >= 90000) {
            errors.add("E040: Spark Plug Replacement")
            errors.add("E041: Timing Belt Inspection")
            errors.add("E042: Water Pump Inspection")
        }

        // 100k miles
        if (miles >= 100000) {
            errors.add("E050: Coolant Service (2nd Cycle)")
            errors.add("E051: Serpentine Belt Replacement")
        }

        // 150k miles
        if (miles >= 150000) {
            errors.add("E060: Timing Belt + Water Pump Replacement")
            errors.add("E061: Suspension Overhaul Needed")
            errors.add("E062: Radiator Replacement")
        }

        // 200k miles
        if (miles >= 200000) {
            errors.add("E070: Fuel Pump Check")
            errors.add("E071: Alternator Inspection")
            errors.add("E072: Engine Mount Inspection")
            errors.add("E073: Transmission Service")
        }

        // 250k+ miles
        if (miles >= 250000) {
            errors.add("E080: Compression Test Recommended")
            errors.add("E081: Sensor Replacement Check")
            errors.add("E082: Rust Inspection Required")
        }

        return errors
    }
}