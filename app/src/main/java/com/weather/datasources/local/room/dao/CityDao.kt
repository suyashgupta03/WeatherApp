package com.weather.datasources.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.weather.datasources.local.room.models.RMCity

@Dao
interface CityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(city: RMCity): Long

    @Query("SELECT COUNT(*) FROM rmcity")
    fun getCount(): Int

    @Query("SELECT * FROM rmcity")
    fun getAllCities(): List<RMCity>
}