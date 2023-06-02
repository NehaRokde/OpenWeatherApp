package com.app.openweatherapp.data.api

import com.app.openweatherapp.Helper
import com.app.openweatherapp.utils.Constants
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class WeatherApiServiceTest {

    lateinit var mockWebServer: MockWebServer

    lateinit var weatherApiService: WeatherApiService

    @Before
    fun setup(){
        mockWebServer = MockWebServer()
        weatherApiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(WeatherApiService::class.java)
    }

    @Test
    fun testGetWeather_returnWeatherResponse() = runTest {

        val cityName = "New York"

        val mockResponse = MockResponse()
        val content = Helper.readFileResource("/response.json")
        mockResponse.setResponseCode(200)
        mockResponse.setBody(content)
        mockWebServer.enqueue(mockResponse)

        val response = weatherApiService.getWeather(cityName)
        mockWebServer.takeRequest()

        Assert.assertEquals(false, response.body().toString().isEmpty())
        Assert.assertEquals("New York", response.body()!!.name)
    }

    @Test
    fun testGetWeather_returnError() = runTest {

        val cityName = "New York"

        val mockResponse = MockResponse()
        mockResponse.setResponseCode(404)
        mockResponse.setBody("City Not Found")
        mockWebServer.enqueue(mockResponse)

        val response = weatherApiService.getWeather(cityName)
        mockWebServer.takeRequest()

        Assert.assertEquals(false, response.isSuccessful)
        Assert.assertEquals(404, response.code())
    }

    @Test
    fun testGetCurrentLocationWeather_returnWeatherResponse() = runTest {

        val latitude = -65.9988
        val longitude = 46.5001

        val mockResponse = MockResponse()
        val content = Helper.readFileResource("/response.json")
        mockResponse.setResponseCode(200)
        mockResponse.setBody(content)
        mockWebServer.enqueue(mockResponse)

        val response = weatherApiService.getCurrentLocationWeather(latitude,longitude)
        mockWebServer.takeRequest()

        Assert.assertEquals(false, response.body().toString().isEmpty())
    }

    @Test
    fun testGetCurrentLocationWeather_returnError() = runTest {

        val latitude = -65.9988
        val longitude = 46.5001

        val mockResponse = MockResponse()
        mockResponse.setResponseCode(404)
        mockResponse.setBody("City Not Found")
        mockWebServer.enqueue(mockResponse)

        val response = weatherApiService.getCurrentLocationWeather(latitude,longitude)
        mockWebServer.takeRequest()

        Assert.assertEquals(false, response.isSuccessful)
        Assert.assertEquals(404, response.code())
    }



    @After
    fun tearDown(){
        mockWebServer.shutdown()
    }

}