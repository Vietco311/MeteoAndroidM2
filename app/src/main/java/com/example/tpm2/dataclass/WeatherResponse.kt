package com.example.tpm2.dataclass

data class WeatherResponse(
    val name: String,
    val main: Main,
    val weather: List<Weather>
)

data class CitySuggestion(
    val name: String,
    val country: String,
    val lon: Double,
    val lat: Double
)

data class ForecastResponse(
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: List<WeatherForecast>,
)

data class WeatherForecast(
    val main: Main,
    val weather: List<Weather>,
    val dt_txt: String
)

data class Main(
    val temp: Float,
)

data class Weather(
    val description: String,
    val icon: String
)

data class WeatherIcon(
    val icon: String
)