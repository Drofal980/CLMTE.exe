package com.clmte_exe.app

class SetUpLogic {

    // How the user will setup their car.
    enum class SetupMethod {
        VIN,
        MANUAL
    }

    // Main car model used.
    data class Car(
        val nickName: String?,
        val carType: String?,
        val make: String?,
        val model: String?,
        val year: Int?,
        val engineSize: Float?,
        val carVin: String?
    )

    // If the user uses a VIN.
    fun setupWithVin(
        nickName: String,
        carVin: String
    ): Result<Car>{
        if (nickName.isBlank()){
            return Result.failure(
                IllegalArgumentException("Nickname is required")
            )
        }

        if (carVin.length != 17) {
            return Result.failure(
                IllegalArgumentException("VIN must be 17 characters long")
            )
        }
        return Result.success(
            Car(
                nickName = String(),
                carType = null,
                make = null,
                model = null,
                year = null,
                engineSize = null,
                carVin = carVin
            )
        )
    }
    // If the user wants to manual input everything.
    // Make, Model, and Year will be required.
    // Everything else if optional for the user to input.
    fun setupManual(
        nickName: String,
        carType: String,
        make: String,
        model: String,
        year: Int,
        engineSize: Float? = null,
        ): Result<Car> {

        if (make.isBlank())
        {
            return Result.failure(
                IllegalArgumentException("Make of Car is required")
            )
        }

        if (carType.isBlank())
        {
            return Result.failure(
                IllegalArgumentException("Type of Car is required")
            )
        }
        // Can't use isBlank on int. Cars aren't older than 1800
        if (year < 1800)
        {
            return Result.failure(
                IllegalArgumentException("Year is required")
            )
        }

        return Result.success(
            Car(
                nickName = nickName,
                carType = carType,
                make = make,
                model = model,
                year = year,
                engineSize = engineSize,
                carVin = null
            )
        )
    }
}