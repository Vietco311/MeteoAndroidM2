package com.example.tpm2.dataclass

data class WeatherVisuals(
    val imageRes: Int,  // Resource ID pour l'image (R.drawable.xxx)
    val backgroundColor: Int // Couleur dominante
)

//fun getWeatherVisuals(code: Int?): WeatherVisuals {
//    return when (code) {
//        0 -> WeatherVisuals(R.drawable.clear_sky, Color.parseColor("#87CEEB")) // Ciel clair
//        1, 2, 3 -> WeatherVisuals(R.drawable.partly_cloudy, Color.parseColor("#B0C4DE")) // Partiellement nuageux
//        45, 48 -> WeatherVisuals(R.drawable.fog, Color.parseColor("#778899")) // Brouillard
//        51, 53, 55 -> WeatherVisuals(R.drawable.drizzle, Color.parseColor("#4682B4")) // Bruine
//        56, 57 -> WeatherVisuals(R.drawable.freezing_drizzle, Color.parseColor("#5F9EA0")) // Bruine gelée
//        61, 63, 65 -> WeatherVisuals(R.drawable.rain, Color.parseColor("#4169E1")) // Pluie
//        66, 67 -> WeatherVisuals(R.drawable.freezing_rain, Color.parseColor("#4682B4")) // Pluie gelée
//        71, 73, 75 -> WeatherVisuals(R.drawable.snow, Color.parseColor("#FFFFFF")) // Neige
//        77 -> WeatherVisuals(R.drawable.snow_grains, Color.parseColor("#E8F0F2")) // Grains de neige
//        80, 81, 82 -> WeatherVisuals(R.drawable.rain_showers, Color.parseColor("#00BFFF")) // Averses de pluie
//        85, 86 -> WeatherVisuals(R.drawable.snow_showers, Color.parseColor("#E0FFFF")) // Averses de neige
//        95 -> WeatherVisuals(R.drawable.thunderstorm, Color.parseColor("#4B0082")) // Orage
//        96, 99 -> WeatherVisuals(R.drawable.thunderstorm_hail, Color.parseColor("#2F4F4F")) // Orage avec grêle
//        else -> WeatherVisuals(R.drawable.unknown, Color.parseColor("#808080")) // Conditions inconnues
//    }
//}
