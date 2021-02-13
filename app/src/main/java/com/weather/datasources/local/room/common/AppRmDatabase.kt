package com.weather.datasources.local.room.common

import androidx.room.Database
import androidx.room.RoomDatabase
import com.weather.datasources.local.room.dao.*
import com.weather.datasources.local.room.models.*

const val APP_DB_NAME = "db-weather"
const val APP_DB_VERSION = 1

@Database(
    entities = [RMWeatherForecast::class, RMCity::class, RMWeather::class,
        RMWeatherDay::class, RMTemp::class, RMForecastWithDaysRef::class, RMDayWithWeathersRef::class],
    version = APP_DB_VERSION
)
abstract class AppRmDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao
    abstract fun weatherForecastDao(): WeatherForecastDao
    abstract fun weatherDayDao(): WeatherDayDao
    abstract fun tempDao(): TempDao
    abstract fun weatherDao(): WeatherDao

    abstract fun weatherForecastWithDaysRefDao(): RMForecastWithDaysRefDao
    abstract fun weatherDayWithWeathersRefDao(): RMDayWithWeathersRefDao
}