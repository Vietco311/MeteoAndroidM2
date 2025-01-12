package com.example.tpm2.api

import com.example.tpm2.dataclass.CitySuggestion
import com.example.tpm2.dataclass.ForecastResponse
import com.example.tpm2.dataclass.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApi {
    @GET("data/2.5/weather")
    fun getWeatherByCity(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String,
        @Query("lang") language: String = "fr",
        @Query("units") units: String = "metric"
    ): Call<WeatherResponse>

    @GET("geo/1.0/direct")
    suspend fun getCitySuggestions(
        @Query("q") cityName: String,
        @Query("limit") limit: Int = 5,
        @Query("appid") apiKey: String
    ): List<CitySuggestion>

    @GET("data/2.5/forecast")
    fun getForecastByCoord(
        @Query("lon") longitude: Double,
        @Query("lat") latitude: Double,
        @Query("appid") apiKey: String,
        @Query("lang") language: String = "fr",
        @Query("units") units: String = "metric"
    ): Call<ForecastResponse>

}
