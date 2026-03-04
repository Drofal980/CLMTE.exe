package com.clmte_exe.app.obj_classes

import java.time.LocalDate

data class ServiceLog(
    var serviceName: String,
    var odometerAtService: Int,
    var dateOfService: LocalDate
)