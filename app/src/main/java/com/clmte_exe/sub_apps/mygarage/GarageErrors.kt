package com.clmte_exe.sub_apps.mygarage

import android.os.Build
import com.clmte_exe.app.obj_classes.MaintenanceRules
import com.clmte_exe.app.obj_classes.ServiceLog as ObjServiceLog
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// We can add more errors and other things if needed
// This holds all the error if the car needs to be inspected
object InspectionManager {

    fun getInspectionErrors(car: Vehicle): List<String> {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return emptyList()
        }

        val rules = MaintenanceRules()
        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")

        val objLogs = car.service_history.mapNotNull { log ->
            try {
                ObjServiceLog(
                    serviceName = log.description,
                    odometerAtService = log.mileage,
                    dateOfService = LocalDate.parse(log.date, formatter)
                )
            } catch (e: Exception) {
                null
            }
        }

        val recommendations = rules.getRecommendations(
            currentOdometer = car.odometer,
            logs = objLogs
        )

        return recommendations.map { "${it.serviceName}: ${it.reason}" }
    }
}
