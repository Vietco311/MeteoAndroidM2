package com.example.tpm2.api

import com.example.tpm2.dataclass.MeteoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenMeteoApi {
    @GET("v1/forecast")
    fun getWeatherForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("timezone") timezone: String = "auto",
        @Query("forecast_days") fd: Int = 3,
        @Query("hourly") hourly: String = "temperature_2m,precipitation_probability,precipitation,cloud_cover,weather_code"
    ): Call<MeteoResponse>
}