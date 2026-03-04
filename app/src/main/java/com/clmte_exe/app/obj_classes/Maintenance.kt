package com.clmte_exe.app.obj_classes
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class MaintenanceRule(
    var name: String,
    var mileageInterval: Int?,
    var timeIntervalMonths: Int?
)
data class MaintenanceRecommendation(
    val serviceName: String,
    val reason: String
)

class MaintenanceRules {
    private val rules = listOf(
        MaintenanceRule("Oil Change", 5000, 6),
        MaintenanceRule("Tire Rotation", 6000, 6),
        MaintenanceRule("Engine Air Filter", 20000, 24),
        MaintenanceRule("Cabin Air Filter", 15000, 12),
        MaintenanceRule("Brake Fluid", 30000, 36),
        MaintenanceRule("Transmission Fluid", 50000, 48),
        MaintenanceRule("Coolant", 75000, 60),
        MaintenanceRule("Spark Plugs", 90000, 84),
        MaintenanceRule("Serpentine Belt", 90000, 72),
        MaintenanceRule("Timing Belt", 100000, 84),
        MaintenanceRule("Differential/Transfer Case Fluid", 50000, 48),
        MaintenanceRule("Brake Pads/Rotors", 40000, 36),
        MaintenanceRule("Battery", null, 48),
        MaintenanceRule("Tires", 60000, 72),
        MaintenanceRule("Suspension Components", 100000, 96)
    )

    fun getRules() = rules

    @RequiresApi(Build.VERSION_CODES.O)
    fun getRecommendations(
        currentOdometer: Int,
        logs: List<ServiceLog>,
        today: LocalDate = LocalDate.now()
    ): List<MaintenanceRecommendation> {

        val recs = mutableListOf<MaintenanceRecommendation>()

        for (rule in rules) {
            val lastService = logs
                .filter { it.serviceName == rule.name }
                .maxByOrNull { it.dateOfService }

            val needsByMileage = rule.mileageInterval?.let { interval ->
                lastService?.let { currentOdometer - it.odometerAtService >= interval }
                    ?: (currentOdometer >= interval)
            } ?: false

            val needsByTime = rule.timeIntervalMonths?.let { interval ->
                lastService?.let {
                    ChronoUnit.MONTHS.between(it.dateOfService, today) >= interval
                } ?: true
            } ?: false

            if (needsByMileage || needsByTime) {
                val reason = when {
                    needsByMileage && needsByTime -> "Due by mileage and time"
                    needsByMileage -> "Due by mileage interval"
                    else -> "Due by time interval"
                }

                recs.add(MaintenanceRecommendation(rule.name, reason))
            }
        }

        return recs
    }
}
