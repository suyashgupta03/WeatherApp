package com.weather.datasources.local.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.weather.entities.weather.Weather

@Entity
data class RMWeather(
    @PrimaryKey (autoGenerate = false) val id: Long = 0,
    val main: String,
    val description: String,
    val icon: String?
)
fun RMWeather.toData(): Weather {
    return Weather(id, main, description, icon)
}
