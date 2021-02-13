package com.weather.datasources.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.weather.datasources.local.room.models.RMWeatherForecast

@Dao
interface WeatherForecastDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(rmWeatherForecast: RMWeatherForecast): Long

    @Query("SELECT * FROM rmweatherforecast WHERE cityId = :cityId")
    fun getWeatherForecastList(cityId:Int): RMWeatherForecast
}