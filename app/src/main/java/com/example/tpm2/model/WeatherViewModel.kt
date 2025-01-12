package com.example.tpm2.model

import RetrofitClient
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpm2.dataclass.CitySuggestion
import com.example.tpm2.dataclass.ForecastResponse
import com.example.tpm2.dataclass.WeatherForecast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class WeatherViewModel : ViewModel() {

    private val _weatherInfo = MutableStateFlow<String>("")
    private val _tempInfo = MutableStateFlow<String>("")
    private val _city = MutableStateFlow<String>("")
    private val _icon = MutableStateFlow<String>("")
    private val _citySuggestions = MutableStateFlow<List<CitySuggestion>>(emptyList())
    private val _selectedForecast = MutableStateFlow<List<WeatherForecast>>(emptyList())
    private val _searchQuery = MutableStateFlow<String>("")

    val weatherInfo: StateFlow<String> get() = _weatherInfo
    val citySuggestions: StateFlow<List<CitySuggestion>> get() = _citySuggestions
    val icon: StateFlow<String> get() = _icon
    val tempInfo: StateFlow<String> get() = _tempInfo
    val selectedForecast: StateFlow<List<WeatherForecast>> get() = _selectedForecast
    val searchQuery: StateFlow<String> get() = _searchQuery
    val city: StateFlow<String> get() = _city

    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchWeather(cityName: String, apiKey: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.api1.getWeatherByCity(
                    cityName = cityName,
                    apiKey = apiKey,
                ).awaitResponse()

                if (response.isSuccessful) {
                    response.body()?.let { weatherResponse ->
                        _tempInfo.value = "${weatherResponse.main.temp} °C"
                        _weatherInfo.value = weatherResponse.weather[0].description
                        _icon.value = "https://openweathermap.org/img/wn/${weatherResponse.weather[0].icon}@2x.png"
                    } ?: run {
                        postError("Données météo introuvables")
                    }
                } else {
                    postError("Ville non trouvée (code ${response.code()})")
                }
            } catch (e: Exception) {
                Log.e("API Error", e.message.orEmpty())
                postError("Erreur lors de l'appel API : ${e.message}")
            }
        }
    }

    private fun fetchCitySuggestions(query: String, apiKey: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val suggestions = RetrofitClient.api1.getCitySuggestions(
                    cityName = query,
                    apiKey = apiKey
                )
                _citySuggestions.value = suggestions
            } catch (e: Exception) {
                Log.e("API Error", e.message.orEmpty())
                _citySuggestions.value = emptyList()
            }
        }
    }

    private fun fetchForecast(lon: Double, lat: Double, apiKey: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.api1.getForecastByCoord(
                    longitude = lon,
                    latitude = lat,
                    apiKey = apiKey
                ).awaitResponse()

                if (response.isSuccessful) {
                    _selectedForecast.value = response.body()?.list!!
                } else {
                    Log.e("API Error", "Prévisions non trouvées : code ${response.code()}")
                    _selectedForecast.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("API Error", e.message.orEmpty())
                _selectedForecast.value = emptyList()
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        Log.d("WeatherViewModel", "Search query updated: $query")
        if (query.isEmpty()) {
            _citySuggestions.value = emptyList()
        } else {
            fetchCitySuggestions(query, "9779ca89225a8c141b03712524b48996")
        }

    }

    fun clearSearch() {
        _citySuggestions.value = emptyList()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun selectCity(city: CitySuggestion) {
        updateSearchQuery("")
        _city.value = city.name
        fetchWeather(city.name, "9779ca89225a8c141b03712524b48996")
        fetchForecast(city.lon, city.lat, "9779ca89225a8c141b03712524b48996")
    }

    private fun postError(message: String) {
        _weatherInfo.value = message
        _tempInfo.value = "N/A"
        _icon.value = ""
    }
}
