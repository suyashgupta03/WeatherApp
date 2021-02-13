package com.weather.datasources.local.room

import com.weather.entities.weather.WeatherForecast

interface DbDataSource {
    suspend fun saveWeatherForecast(forecast: WeatherForecast)
    suspend fun getWeatherForecast(): WeatherForecast?
    suspend fun clearAllData()
}