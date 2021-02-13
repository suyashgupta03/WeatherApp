package com.weather.datasources.api

import com.weather.entities.common.AppResult
import com.weather.entities.weather.WeatherForecast

interface ApiDataSource {
    suspend fun fetchWeatherForecast(city: String, count:Int, unit: String): AppResult<WeatherForecast>
}