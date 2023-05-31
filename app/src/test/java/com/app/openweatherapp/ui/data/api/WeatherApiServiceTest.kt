package com.app.openweatherapp.ui.data.api

import com.app.openweatherapp.data.api.WeatherApiService
import com.app.openweatherapp.data.model.WeatherResponse
import com.app.openweatherapp.utils.Constants
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherApiServiceTest {

    private lateinit var server: MockWebServer
    private lateinit var apiService: WeatherApiService

    @Before
    fun setup() {
        server = MockWebServer()
        server.start()

        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(WeatherApiService::class.java)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun getWeather_shouldReturnWeatherResponse() = runBlocking {
        // Arrange
        val responseJson = """
            {
                "coord": {
                    "lon": -65.9988,
                    "lat": 46.5001
                },
                "weather": [{
                    "id": 801,
                    "main": "Clouds",
                    "description": "few clouds",
                    "icon": "02d"
                }],
                "base": "stations",
                "main": {
                    "temp": 29.75,
                    "feels_like": 28.83,
                    "temp_min": 29.75,
                    "temp_max": 29.75,
                    "pressure": 1015,
                    "humidity": 34,
                    "sea_level": 1015,
                    "grnd_level": 999
                },
                "visibility": 10000,
                "wind": {
                    "speed": 3.74,
                    "deg": 261,
                    "gust": 4.11
                },
                "clouds": {
                    "all": 11
                },
                "dt": 1685565628,
                "sys": {
                    "country": "CA",
                    "sunrise": 1685522148,
                    "sunset": 1685578070
                },
                "timezone": -10800,
                "id": 6087430,
                "name": "New Brunswick",
                "cod": 200
           }
        """.trimIndent()

        server.enqueue(MockResponse().setBody(responseJson))

        // Act
        val cityName = "New Brunswick"
        val result = apiService.getWeather(cityName)

        // Assert
        val request: RecordedRequest = server.takeRequest()
        val expectedPath = "/weather?q=$cityName&appid=${Constants.API_KEY}&units=${Constants.UNITS}"
        assert(request.path == expectedPath)
        assert(result.name == "New Brunswick")
        assert(result.weather[0].main == "Clouds")
        assert(result.weather[0].description == "few clouds")
        assert(result.main.temp == 29.75)
    }
}
