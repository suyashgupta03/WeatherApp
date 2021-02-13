package com.weather.datasources.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.weather.datasources.local.room.models.RMWeather

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(weather: RMWeather): Long

    @Query("SELECT * FROM rmweather WHERE id IN (:ids)")
    fun getWeatherList(ids: List<Long>): List<RMWeather>
}