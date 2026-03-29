package com.clmte_exe.app.obj_classes

class Vehicle(
    private var id: String? = null,
    private var make: String? = null,
    private var model: String? = null,
    private var year: Int? = null,
    private var bodyClass: String? = null,
    private var engineCylinders: String? = null,
    private var engineDisplacement: String? = null,
    private var driveType: String? = null,
    private var transmission: String? = null,
    private var vin_num: String? = null,
    private var odometer: Long? = null,
    private var service_logs: List<ServiceLog>? = null,
) {
    // Getters and Setters
    fun getId() = id
    fun setId(value: String?) { id = value }

    fun getMake() = make
    fun setMake(value: String?) { make = value }

    fun getModel() = model
    fun setModel(value: String?) { model = value }

    fun getYear() = year
    fun setYear(value: Int?) { year = value }

    fun getBodyClass() = bodyClass
    fun setBodyClass(value: String?) { bodyClass = value }

    fun getEngineCylinders() = engineCylinders
    fun setEngineCylinders(value: String?) { engineCylinders = value }

    fun getEngineDisplacement() = engineDisplacement
    fun setEngineDisplacement(value: String?) { engineDisplacement = value }

    fun getDriveType() = driveType
    fun setDriveType(value: String?) { driveType = value }

    fun getTransmission() = transmission
    fun setTransmission(value: String?) { transmission = value }

    fun getVinNum() = vin_num
    fun setVinNum(value: String?) { vin_num = value }

    fun getOdometer() = odometer
    fun setOdometers(value: Long?) { odometer = value }

    fun getServices() = service_logs
    fun setServices(value: List<ServiceLog>) { service_logs = value }
    fun addService(logs: List<ServiceLog>) { service_logs = logs }

    fun exportAsJson() {
        // TODO: Implement
    }
    fun importFromJson() {
        // TODO: Implement
    }

    override fun toString(): String {
        return "Vehicle(year=$year, make=$make, model=$model, bodyClass=$bodyClass, " +
                "engineCylinders=$engineCylinders, engineDisplacement=$engineDisplacement, " +
                "driveType=$driveType, transmission=$transmission)"
    }
}