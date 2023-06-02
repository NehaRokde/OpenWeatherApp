package com.app.openweatherapp.ui.screens.search

import com.app.openweatherapp.data.model.*
import com.app.openweatherapp.data.repository.WeatherRepository
import com.app.openweatherapp.ui.screens.home.HomeState
import com.app.openweatherapp.utils.NetworkResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SearchCityViewModelTest {


    private lateinit var weatherRepository: WeatherRepository
    private lateinit var searchCityViewModel: SearchCityViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        weatherRepository = mockk()
        searchCityViewModel = SearchCityViewModel(weatherRepository)
        Dispatchers.setMain(testDispatcher)

    }


    @Test
    fun test_getWeatherData_success() = runBlocking {
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

        coEvery { weatherRepository.getWeather(cityName) } returns NetworkResult.Success(expectedWeatherResponse)
        searchCityViewModel.getWeatherData(cityName)
        testDispatcher.scheduler.advanceUntilIdle()

        val weatherData = searchCityViewModel.weatherData.value
        val actualWeatherResponse = weatherData?.data

        assertEquals(HomeState(data = expectedWeatherResponse), searchCityViewModel.weatherData.value)
        assertEquals("New York", actualWeatherResponse?.name)

    }

    @Test
    fun test_getWeatherData_error() = runBlocking {
        val cityName = "InvalidCity"
        val errorMessage = "City Not Found"

        coEvery { weatherRepository.getWeather(cityName) } returns NetworkResult.Error(errorMessage)
        searchCityViewModel.getWeatherData(cityName)
        testDispatcher.scheduler.advanceUntilIdle()

        val weatherData = searchCityViewModel.weatherData.value
        val actualError = weatherData?.error

        assertEquals(errorMessage, actualError)

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

}