package com.clmte_exe.app.api_calls

import com.clmte_exe.app.obj_classes.Vehicle
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import okhttp3.OkHttpClient

// Retrofit Models
data class VinDecodeResponse(val Results: List<VinResult>)
data class VinResult(val Variable: String, val Value: String?)

interface NhtsaApiService {
    @GET("vehicles/decodevin/{vin}")
    suspend fun decodeVin(
        @Path("vin") vin: String,
        @Query("format") format: String = "json"
    ): VinDecodeResponse
}

class NhtsaApi {

    private val okHttpClient = OkHttpClient.Builder().build()

    private val api: NhtsaApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://vpic.nhtsa.dot.gov/api/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NhtsaApiService::class.java)
    }

    suspend fun fetchVehicle(vin: String): Vehicle {
        val response = api.decodeVin(vin)

        fun getValue(name: String): String? =
            response.Results.firstOrNull { it.Variable.equals(name, ignoreCase = true) }?.Value

        // Validation: Check NHTSA Error Code
        val errorCode = getValue("Error Code")
        if (errorCode != null && errorCode != "0") {
            val errorText = getValue("Error Text") ?: "Unknown API Error"
            throw Exception("NHTSA Error: $errorText (Code $errorCode)")
        }

        val make = getValue("Make")
        val model = getValue("Model")
        val yearString = getValue("Model Year")
        val year = yearString?.toIntOrNull()

        // Validation: Ensure essential data is present
        if (make.isNullOrBlank() || model.isNullOrBlank() || year == null) {
            throw Exception("Incomplete vehicle data returned from NHTSA.")
        }

        val rawBodyClass = getValue("Body Class")
        val normalizedBodyClass = when {
            rawBodyClass?.contains("pickup", ignoreCase = true) == true || rawBodyClass?.contains("truck", ignoreCase = true) == true -> "Truck"
            rawBodyClass?.contains("suv", ignoreCase = true) == true || rawBodyClass?.contains("sport utility", ignoreCase = true) == true || rawBodyClass?.contains("crossover", ignoreCase = true) == true -> "SUV"
            else -> "Sedan"
        }

        val rawTrans = getValue("Transmission Style")
        val normalizedTrans = if (rawTrans?.contains("manual", ignoreCase = true) == true) "Manual" else "Automatic"

        val rawDrive = getValue("Drive Type")
        val normalizedDrive = when {
            rawDrive?.contains("4wd", ignoreCase = true) == true || rawDrive?.contains("4x4", ignoreCase = true) == true -> "4WD"
            rawDrive?.contains("awd", ignoreCase = true) == true || rawDrive?.contains("all wheel", ignoreCase = true) == true -> "AWD"
            rawDrive?.contains("rwd", ignoreCase = true) == true || rawDrive?.contains("rear wheel", ignoreCase = true) == true -> "RWD"
            else -> "FWD"
        }

        return Vehicle(
            make = make,
            model = model,
            year = year,
            bodyClass = normalizedBodyClass,
            engineCylinders = getValue("Engine Number of Cylinders"),
            engineDisplacement = getValue("Displacement (L)"),
            driveType = normalizedDrive,
            transmission = normalizedTrans
        )
    }
}