package com.weather.datasources.local.room

import com.weather.datasources.local.room.common.AppRmDatabase
import com.weather.datasources.local.room.models.RMDayWithWeathersRef
import com.weather.datasources.local.room.models.RMForecastWithDaysRef
import com.weather.datasources.local.room.models.toData
import com.weather.entities.weather.WeatherForecast
import com.weather.entities.weather.toRMEntity

class RoomDataSource(private val database: AppRmDatabase) : DbDataSource {

    override suspend fun saveWeatherForecast(forecast: WeatherForecast) {
        database.cityDao().insert(forecast.city.toRMEntity())
        val forecastId = database.weatherForecastDao().insert(forecast.toRMEntity())
        forecast.weatherDay.forEach { weatherDay ->
            val tempId = database.tempDao().insert(weatherDay.temp.toRMEntity())
            val feelsLikeId = database.tempDao().insert(weatherDay.feelsLike.toRMEntity())
            val dayId = database.weatherDayDao().insert(weatherDay.toRMEntity(tempId, feelsLikeId))
            database.weatherForecastWithDaysRefDao()
                .insert(RMForecastWithDaysRef(forecastId, dayId))//ref
            weatherDay.weather.forEach { weather ->
                val weatherId = database.weatherDao().insert(weather.toRMEntity())
                database.weatherDayWithWeathersRefDao()
                    .insert(RMDayWithWeathersRef(dayId, weatherId)) //ref
            }
        }
    }

    override suspend fun getWeatherForecast(): WeatherForecast? {
        if (database.cityDao().getCount() > 0) {
            val city = database.cityDao().getAllCities().first().toData()
            val rmWeatherForecast = database.weatherForecastDao().getWeatherForecastList(city.id)
            val weatherDays =
                database.weatherForecastWithDaysRefDao().getWeatherForecastIds(rmWeatherForecast.id)
                    .map { dayId ->
                        val rmWeatherDay = database.weatherDayDao().getWeatherById(dayId)
                        val temp = database.tempDao().getTempById(rmWeatherDay.tempId).toData()
                        val feelsLike =
                            database.tempDao().getTempById(rmWeatherDay.feelsLikeId).toData()
                        val weatherIds =
                            database.weatherDayWithWeathersRefDao().getWeatherIds(rmWeatherDay.date)
                        val weathers =
                            database.weatherDao().getWeatherList(weatherIds).map { it.toData() }
                        rmWeatherDay.toData(temp = temp, feelsLike = feelsLike, weathers)
                    }.toMutableList()
            return rmWeatherForecast.toData(city, weatherDays)
        }
        return null
    }

    override suspend fun clearAllData() {
        database.clearAllTables()
    }
}