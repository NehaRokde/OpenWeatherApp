package com.app.openweatherapp.ui.screens.home

import com.app.openweatherapp.data.model.WeatherResponse

// class to handle state of api result
data class HomeState(
    val isLoading: Boolean = false,
    val data: WeatherResponse? = null,
    val error: String = ""

)
