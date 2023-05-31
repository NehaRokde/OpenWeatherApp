package com.app.openweatherapp.data.repository

import com.app.openweatherapp.data.api.WeatherApiService
import com.app.openweatherapp.data.model.WeatherResponse
import com.app.openweatherapp.utils.Resource
import javax.inject.Inject

//a repository class that handles data operations and acts as a single source of truth:
class WeatherRepository @Inject constructor(private val weatherApiService: WeatherApiService) {

    suspend fun getWeather(cityName: String): Resource<WeatherResponse> {
        return try {
            val result = weatherApiService.getWeather(cityName)
            Resource.Success(data = result)
        } catch (e: java.lang.Exception) {
            Resource.Error(message = e.message.toString())
        }
    }

    suspend fun getWeatherByLocation(
        latitude: Double,
        longitude: Double
    ): Resource<WeatherResponse> {
        return try {
            val result = weatherApiService.getCurrentLocationWeather(latitude, longitude)
            Resource.Success(data = result)
        } catch (e: java.lang.Exception) {
            Resource.Error(message = e.message.toString())
        }
    }

}