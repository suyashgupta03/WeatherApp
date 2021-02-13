package com.weather.interactor.usecases

import com.weather.datasources.api.ApiDataSource
import com.weather.datasources.local.prefs.PrefData
import com.weather.datasources.local.room.DbDataSource
import com.weather.entities.common.AppResult
import com.weather.entities.common.ErrorType
import com.weather.entities.weather.WeatherForecast
import com.weather.ui.common.messages.Message

class WeatherUseCaseImpl(
    private val apiDataSource: ApiDataSource,
    private val dbDataSource: DbDataSource,
    private val prefData: PrefData,
    private val message: Message
) : WeatherUseCase {

    override suspend fun getForecastOfOneWeek(): AppResult<WeatherForecast> {
        val cityName = prefData.getCachedCityName()
        return if (cityName.isEmpty()) {
            AppResult.Error(ErrorType.NO_LOCATION, message.getErrorMessage(ErrorType.NO_LOCATION))
        } else {
            return when(val result = apiDataSource.fetchWeatherForecast(cityName, PARAM_COUNT_WEEK_FORECAST, PARAM_UNIT_DATA_FORECAST)) {
                is AppResult.Success -> {
                    dbDataSource.clearAllData()
                    dbDataSource.saveWeatherForecast(result.latestValue)
                    result
                }
                is AppResult.Empty -> result
                is AppResult.Error -> {
                    if(result.type == ErrorType.NETWORK) {
                        dbDataSource.getWeatherForecast()?.let {
                            AppResult.Success(it)
                        } ?: run {
                            result
                        }
                    } else {
                        result
                    }
                }
            }
        }
    }

    companion object {
        private const val PARAM_COUNT_WEEK_FORECAST = 7
        private const val PARAM_UNIT_DATA_FORECAST = "metric"//or imperial
    }
}