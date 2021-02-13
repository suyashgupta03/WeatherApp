package com.weather.datasources.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.weather.datasources.local.room.models.RMTemp

@Dao
interface TempDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(day: RMTemp): Long

    @Query("SELECT * FROM rmtemp WHERE id = :id")
    fun getTempById(id: Long): RMTemp
}