package com.example.tpm2.dataclass

data class MeteoResponse(
    val hourly: HourlyData
)

data class HourlyData(
    val temperature_2m: List<Double>,
    val precipitation_probability: List<Double>,
    val precipitation: List<Double>,
    val cloud_cover: List<Double>,
    val timezone: String,
    val forecast_days: Int,
    val weather_code: List<Int>
)