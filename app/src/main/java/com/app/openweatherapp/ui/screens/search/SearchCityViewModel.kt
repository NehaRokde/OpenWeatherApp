package com.app.openweatherapp.ui.screens.search

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.openweatherapp.data.repository.WeatherRepository
import com.app.openweatherapp.ui.screens.home.HomeState
import com.app.openweatherapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchCityViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    val weatherData: MutableState<HomeState?> = mutableStateOf(null)

    fun getWeatherData(cityName: String) = viewModelScope.launch {

        when (val result = weatherRepository.getWeather(cityName)) {
            is NetworkResult.Loading -> {
                weatherData.value = HomeState(isLoading = true)

            }
            is NetworkResult.Success -> {
                result.data?.let {
                    weatherData.value = HomeState(data = result.data)
                }
            }
            is NetworkResult.Error -> {
                if (result.message == "404") {
                    weatherData.value = HomeState(error = "City Not Found")
                } else {
                    weatherData.value = HomeState(error = "Something went wrong!!!")
                }
            }
        }
    }

}
