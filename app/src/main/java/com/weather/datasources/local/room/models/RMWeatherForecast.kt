package com.weather.datasources.local.room.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.weather.entities.weather.City
import com.weather.entities.weather.WeatherDay
import com.weather.entities.weather.WeatherForecast

@Entity(foreignKeys = [
    ForeignKey(
        entity = RMCity::class,
        parentColumns = ["id"],
        childColumns = ["cityId"],
        onDelete = ForeignKey.CASCADE
    )
])
data class RMWeatherForecast (
    // change it when default clear logic is removed
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val cityId: Int,
    val cod: Int,
    val message: Float,
    val count: Int
)

fun RMWeatherForecast.toData(city:City, weatherDays: List<WeatherDay>): WeatherForecast {
    return WeatherForecast(city, cod, message, count, weatherDays)
}
