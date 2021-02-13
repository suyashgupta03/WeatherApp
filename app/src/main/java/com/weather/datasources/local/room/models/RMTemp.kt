package com.weather.datasources.local.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.weather.entities.weather.TempDay

@Entity
data class RMTemp(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val night: Float,
    val eve: Float,
    val day: Float,
    val morn: Float
)

fun RMTemp.toData(): TempDay {
    return TempDay(night, eve, day, morn)
}