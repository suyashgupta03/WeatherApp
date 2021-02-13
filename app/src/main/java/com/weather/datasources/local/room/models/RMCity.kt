package com.weather.datasources.local.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.weather.entities.weather.City
import com.weather.entities.weather.LatLon
import com.weather.entities.weather.WeatherForecast

@Entity
data class RMCity(
    @PrimaryKey(autoGenerate = false) val id: Int = 0,
    val name: String,
    val latitude: Float,
    val longitude: Float,
    val country: String
)

fun RMCity.toData(): City {
    return City(id, name, LatLon(latitude, longitude), country)
}
