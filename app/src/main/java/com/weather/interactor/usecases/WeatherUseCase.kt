package com.weather.interactor.usecases

import com.weather.entities.common.AppResult
import com.weather.entities.weather.WeatherForecast

interface WeatherUseCase {
    suspend fun getForecastOfOneWeek(): AppResult<WeatherForecast>
}