package com.weather.datasources.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.weather.datasources.local.room.models.RMForecastWithDaysRef

@Dao
interface RMForecastWithDaysRefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(rmForecastWithDaysRef: RMForecastWithDaysRef): Long

    @Query("SELECT date FROM rmforecastwithdaysref WHERE id = :weatherForecastId")
    fun getWeatherForecastIds(weatherForecastId:Long): List<Long>
}