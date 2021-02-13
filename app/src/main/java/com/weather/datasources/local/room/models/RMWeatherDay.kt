package com.weather.datasources.local.room.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.weather.entities.weather.TempDay
import com.weather.entities.weather.Weather
import com.weather.entities.weather.WeatherDay

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = RMTemp::class,
            parentColumns = ["id"],
            childColumns = ["tempId"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = RMTemp::class,
            parentColumns = ["id"],
            childColumns = ["feelsLikeId"],
            onDelete = CASCADE
        )]
)
data class RMWeatherDay(
    @PrimaryKey(autoGenerate = false) val date: Long,
    val sunrise: Long,
    val sunset: Long,
    val tempId: Long,
    val min: Float,
    val max: Float,
    val feelsLikeId: Long,
    val pressure: Int,
    val humidity: Int,
    val speed: Float,
    val deg: Int,
    val clouds: Int,
    val pop: Float,
    val snow: Float,
)

fun RMWeatherDay.toData(temp: TempDay, feelsLike:TempDay, weatherList: List<Weather>): WeatherDay {
    return WeatherDay(
        date,
        sunrise,
        sunset,
        temp,
        min,
        max,
        feelsLike,
        pressure,
        humidity,
        weatherList,
        speed,
        deg,
        clouds,
        pop,
        snow
    )
}
