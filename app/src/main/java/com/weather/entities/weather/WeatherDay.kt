package com.weather.entities.weather

import android.os.Parcelable
import com.weather.datasources.local.room.models.RMWeatherDay
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeatherDay(
    val date: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: TempDay,
    val min: Float,
    val max: Float,
    val feelsLike: TempDay,
    val pressure: Int,
    val humidity: Int,
    val weather: List<Weather>,
    val speed: Float,
    val deg: Int?,
    val clouds: Int?,
    val pop: Float?,
    val snow: Float?
) : Parcelable

fun WeatherDay.toRMEntity(tempId: Long, feelsLikeId: Long): RMWeatherDay {
    return RMWeatherDay(
        date,
        sunrise,
        sunset,
        tempId,
        min,
        max,
        feelsLikeId,
        pressure,
        humidity,
        speed,
        deg ?: 0,
        clouds ?: 0,
        pop ?: 0.0f,
        snow ?: 0.0f
    )
}