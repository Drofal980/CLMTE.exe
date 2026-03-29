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

        return Vehicle(
            make = getValue("Make"),
            model = getValue("Model"),
            year = getValue("Model Year") as Int?,
            bodyClass = getValue("Body Class"),
            engineCylinders = getValue("Engine Number of Cylinders"),
            engineDisplacement = getValue("Displacement (L)"),
            driveType = getValue("Drive Type"),
            transmission = getValue("Transmission Style")
        )
    }

    fun shutdown() {
        okHttpClient.dispatcher.executorService.shutdown()
        okHttpClient.connectionPool.evictAll()
        okHttpClient.cache?.close()
    }
}