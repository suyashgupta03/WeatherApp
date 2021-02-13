package com.weather.datasources.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.weather.datasources.local.room.models.RMDayWithWeathersRef

@Dao
interface RMDayWithWeathersRefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(rmDayWithWeathersRef: RMDayWithWeathersRef): Long

    @Query("SELECT id FROM rmdaywithweathersref WHERE date = :weatherDayDate")
    fun getWeatherIds(weatherDayDate:Long): List<Long>
}