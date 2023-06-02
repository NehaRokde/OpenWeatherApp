package com.app.openweatherapp.data.repository

import com.app.openweatherapp.data.api.WeatherApiService
import com.app.openweatherapp.data.model.*
import com.app.openweatherapp.utils.NetworkResult
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response


internal class WeatherRepositoryTest {

    @Mock
    lateinit var weatherApiService: WeatherApiService

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun testGetWeather_expectedWeatherResponse() = runTest {
        val cityName = "New York"
        val expectedWeatherResponse = WeatherResponse(
            coord = Coord(-65.9988, 46.5001),
            weather = listOf(Weather("few clouds", "02d", 801, "Clouds")),
            base = "stations",
            main = Main(28.83, 999, 34, 1015, 1015, 29.75, 29.75, 29.75),
            visibility = 10000,
            wind = Wind(261, 4.11, 3.74),
            clouds = Clouds(11),
            dt = 1685565628,
            sys = Sys("CA", 1685522148, 1685578070),
            timezone = -10800,
            id = 6087430,
            name = "New York",
            cod = 200
        )

        Mockito.`when`(weatherApiService.getWeather(cityName))
            .thenReturn(Response.success(expectedWeatherResponse))

        val sut = WeatherRepository(weatherApiService)
        val result = sut.getWeather(cityName)

        Assert.assertEquals(true, result is NetworkResult.Success)
        Assert.assertEquals("New York", result.data!!.name)
        Assert.assertEquals("few clouds", result.data!!.weather[0].description)
        val delta = 0.01
        Assert.assertEquals(28.83, result.data!!.main.feelsLike, delta)
    }

    @Test
    fun testGetWeather_expectedError() = runTest {
        val cityName = "Nt"

        Mockito.`when`(weatherApiService.getWeather(cityName))
            .thenReturn(Response.error(404, "city not found".toResponseBody()))

        val sut = WeatherRepository(weatherApiService)
        val result = sut.getWeather(cityName)

        Assert.assertEquals(true, result is NetworkResult.Error)
        Assert.assertEquals("404", result.message)
    }

    @Test
    fun testGetWeatherByLocation_expectedWeatherResponse() = runTest {

        val latitude = -65.9988
        val longitude = 46.5001
        val expectedWeatherResponse = WeatherResponse(
            coord = Coord(-65.9988, 46.5001),
            weather = listOf(Weather("few clouds", "02d", 801, "Clouds")),
            base = "stations",
            main = Main(28.83, 999, 34, 1015, 1015, 29.75, 29.75, 29.75),
            visibility = 10000,
            wind = Wind(261, 4.11, 3.74),
            clouds = Clouds(11),
            dt = 1685565628,
            sys = Sys("CA", 1685522148, 1685578070),
            timezone = -10800,
            id = 6087430,
            name = "New York",
            cod = 200
        )

        Mockito.`when`(weatherApiService.getCurrentLocationWeather(latitude, longitude))
            .thenReturn(Response.success(expectedWeatherResponse))

        val sut = WeatherRepository(weatherApiService)
        val result = sut.getWeatherByLocation(latitude, longitude)

        Assert.assertEquals(true, result is NetworkResult.Success)
        Assert.assertEquals("New York", result.data!!.name)
        Assert.assertEquals("few clouds", result.data!!.weather[0].description)
        val delta = 0.01
        Assert.assertEquals(28.83, result.data!!.main.feelsLike, delta)
    }


    @Test
    fun testGetWeatherByLocation_expectedError() = runTest {
        val latitude = -65.9988
        val longitude = 46.5001

        Mockito.`when`(weatherApiService.getCurrentLocationWeather(latitude, longitude))
            .thenReturn(Response.error(404, "city not found".toResponseBody()))

        val sut = WeatherRepository(weatherApiService)
        val result = sut.getWeatherByLocation(latitude, longitude)

        Assert.assertEquals(true, result is NetworkResult.Error)
        Assert.assertEquals("404", result.message)
    }
}
