package com.example.tpm2

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tpm2.ui.theme.TpM2Theme
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.tpm2.dataclass.CitySuggestion
import com.example.tpm2.dataclass.ForecastResponse
import com.example.tpm2.dataclass.WeatherForecast
import com.example.tpm2.dataclass.WeatherResponse
import com.example.tpm2.model.WeatherViewModel


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            TpM2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Main(modifier = Modifier.padding(innerPadding), innerPadding)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Main(modifier: Modifier = Modifier, innerPadding: PaddingValues) {
    val viewModel: WeatherViewModel = viewModel()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val citySuggestions by viewModel.citySuggestions.collectAsState()
    val selectedWeatherInfo by viewModel.weatherInfo.collectAsState()
    val forecastList by viewModel.selectedForecast.collectAsState()
    val iconUrl by viewModel.icon.collectAsState()
    val tempInfo by viewModel.tempInfo.collectAsState()
    val city by viewModel.city.collectAsState()

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val screenHeight = maxHeight

        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            Title(modifier = Modifier.padding(bottom = 16.dp))

            // Search bar and suggestions
            CitySearchBar(
                query = searchQuery,
                onQueryChanged = { query ->
                    viewModel.updateSearchQuery(query)
                },
                onClear = { viewModel.clearSearch() },
                citySuggestions = citySuggestions,
                onCitySelected = { city ->
                    viewModel.selectCity(city)
                },
                suggestionHeight = screenHeight * 0.3f
            )

            // Weather and forecast display
            WeatherDisplay(
                weatherDescription = selectedWeatherInfo,
                iconUrl = iconUrl,
                tempInfo = tempInfo,
                cityName = city
            )

            ForecastDisplay(
                forecastList = forecastList
            )
        }
    }
}


@Composable
fun CitySearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    onClear: () -> Unit,
    citySuggestions: List<CitySuggestion>,
    onCitySelected: (CitySuggestion) -> Unit,
    suggestionHeight: Dp
) {
    Column {
        SearchBar(
            query = query,
            onQueryChanged = onQueryChanged,
            onClear = onClear
        )

        if (citySuggestions.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(0.dp, suggestionHeight)
                    .background(Color.White)
                    .padding(8.dp)
            ) {
                items(citySuggestions) { city ->
                    Text(
                        text = "${city.name}, ${city.country}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { onCitySelected(city) }
                    )
                }
            }
        }
    }
}

@Composable
fun WeatherDisplay(cityName: String, iconUrl: String, tempInfo: String, weatherDescription: String) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            if (cityName.isNotEmpty()) {
                Text(text = cityName, fontSize = 25.sp, fontWeight = FontWeight.Bold)
            }
            if (iconUrl.isNotEmpty()) {
                AsyncImage(
                    model = iconUrl,
                    contentDescription = "Weather Image",
                    modifier = Modifier.size(200.dp)
                )
            }
            if (tempInfo.isNotEmpty()) {
                Text(text = tempInfo, fontSize = 40.sp, fontWeight = FontWeight.Bold)
            }
            if (weatherDescription.isNotEmpty()) {
                Text(text = weatherDescription, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
}


@Composable
fun ForecastDisplay(forecastList: List<WeatherForecast>) {
    LazyRow {
        items(forecastList) { forecastItem ->
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .background(Color.LightGray)
                    .padding(16.dp)
            ) {
                Text(text = "Date: ${forecastItem.dt_txt}")
                Text(text = "Temp: ${forecastItem.main.temp}°C")
                Text(text = "Météo: ${forecastItem.weather[0].description}")
            }
        }
    }
}

@Composable
fun Title(modifier: Modifier = Modifier) {
    Text(
        text = "Météo",
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier.padding(16.dp)
    )
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChanged: (String) -> Unit,
    onClear: () -> Unit
) {
    TextField(
        value = query,
        onValueChange = {
            onQueryChanged(it)  // Vérifier que cette méthode est bien appelée
        },
        placeholder = { Text(text = "Rechercher une ville...") },
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon"
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onClear() }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear Icon"
                    )
                }
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.LightGray,
            unfocusedContainerColor = Color.Gray,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .onGloballyPositioned { coordinates ->
                Log.d("SearchBar", "Search bar size: ${coordinates.size}")
            }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun MainPreview() {
    TpM2Theme {
        Main(
            innerPadding = PaddingValues(16.dp)
        )
    }
}