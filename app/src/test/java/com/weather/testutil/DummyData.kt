package com.weather.testutil

import com.weather.entities.weather.City
import com.weather.entities.weather.LatLon
import com.weather.entities.weather.WeatherForecast

object DummyData {

    const val cityName = "Leipzig"
    private const val count = 7

    private val city: City = City(1, cityName, LatLon(56.67f, 67.34f), "Germany")

    fun weatherForecast(): WeatherForecast {
        return WeatherForecast(city, 23, 35.4f, count, listOf())
    }

    fun noCityErrMessage(): String {
        return "noCitySet"
    }
}