package com.app.openweatherapp.ui.viewmodel

import android.location.Location
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.app.openweatherapp.data.location.LocationTracker
import com.app.openweatherapp.data.model.WeatherResponse
import com.app.openweatherapp.data.repository.WeatherRepository
import com.app.openweatherapp.ui.screens.home.HomeState
import com.app.openweatherapp.ui.screens.home.HomeViewModel
import com.app.openweatherapp.utils.Resource
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.io.BufferedReader
import java.io.InputStreamReader

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var locationTracker: LocationTracker

    @Mock
    private lateinit var weatherRepository: WeatherRepository

    private lateinit var homeViewModel: HomeViewModel

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)

        homeViewModel = HomeViewModel(locationTracker, weatherRepository)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun getCurrentLocation_withValidLocation_shouldFetchWeatherData()= runBlocking {
        // Arrange
        val latitude = 37.7749
        val longitude = -122.4194
        val location = Location("TestProvider").apply {
            this.latitude = latitude
            this.longitude = longitude
        }

        `when`(locationTracker.getCurrentLocation()).thenReturn(location)
        val gson = Gson()
        val jsonString = getResourceFileAsString("com/app/openweatherapp/data/model/weatherResponse.json")
        val weatherResponse = gson.fromJson(jsonString, WeatherResponse::class.java)

        val successResource = Resource.Success(weatherResponse)
        `when`(weatherRepository.getWeatherByLocation(latitude, longitude)).thenReturn(successResource)

        val expectedState = HomeState(data = weatherResponse)

        // Act
        homeViewModel.getCurrentLocation()

        // Assert
        assert(homeViewModel.currentLocation == location)
        assert(homeViewModel.currentLocationWeatherData.value == expectedState)
    }

    @Test
    fun getCurrentLocation_withNullLocation_shouldSetErrorState() = runBlocking {
        // Arrange
        val location: Location? = null
        `when`(locationTracker.getCurrentLocation()).thenReturn(location)

        // Act
        homeViewModel.getCurrentLocation()

        // Assert
        assert(homeViewModel.currentLocation == null)
        assert(homeViewModel.currentLocationWeatherData.value?.error == "Something went wrong")
    }

    private fun getResourceFileAsString(filePath: String): String {
        val inputStream = javaClass.classLoader.getResourceAsStream(filePath)
        val reader = BufferedReader(InputStreamReader(inputStream))
        return reader.use { it.readText() }
    }
}
