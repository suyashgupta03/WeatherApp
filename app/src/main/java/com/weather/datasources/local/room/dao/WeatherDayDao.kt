package com.weather.datasources.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.weather.datasources.local.room.models.RMWeatherDay

@Dao
interface WeatherDayDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(day: RMWeatherDay): Long

    @Query("SELECT * FROM rmweatherday WHERE date = :weatherDayId")
    fun getWeatherById(weatherDayId:Long): RMWeatherDay
}