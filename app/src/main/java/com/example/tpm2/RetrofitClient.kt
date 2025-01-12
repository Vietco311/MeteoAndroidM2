import com.example.tpm2.api.OpenMeteoApi
import com.example.tpm2.api.OpenWeatherApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL_1 = "https://api.openweathermap.org/"
    private const val BASE_URL_2 = "https://api.open-meteo.com/"

    val api1: OpenWeatherApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_1)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenWeatherApi::class.java)
    }

    val api2: OpenMeteoApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_2)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenMeteoApi::class.java)
    }
}
