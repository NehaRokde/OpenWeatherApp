package com.app.openweatherapp.ui.screens.home

import android.location.Location
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.openweatherapp.data.location.LocationTracker
import com.app.openweatherapp.data.repository.WeatherRepository
import com.app.openweatherapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val locationTracker: LocationTracker,
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    val currentLocationWeatherData: MutableState<HomeState?> = mutableStateOf(null)

    fun getCurrentWeatherData(latitude: Double, longitude: Double) = viewModelScope.launch {

        when (val result = weatherRepository.getWeatherByLocation(latitude, longitude)) {
            is Resource.Loading -> {
                currentLocationWeatherData.value = HomeState(isLoading = true)

            }
            is Resource.Success -> {
                result.data?.let {
                    currentLocationWeatherData.value = HomeState(data = result.data)
                }
            }
            is Resource.Error -> {
                currentLocationWeatherData.value = HomeState(error = "Something went wrong")
            }
        }
    }

    var currentLocation by mutableStateOf<Location?>(null)
    fun getCurrentLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            currentLocation = locationTracker.getCurrentLocation()
            Log.e("currentLocation", currentLocation?.latitude.toString())
            Log.e("currentLocation", currentLocation?.longitude.toString())
            if (currentLocation != null) {
                getCurrentWeatherData(currentLocation!!.latitude, currentLocation!!.longitude)
            } else {
                Log.e("Error", "Error")
            }
        }
    }


}
