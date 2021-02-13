package com.weather.entities.weather

import android.os.Parcelable
import com.weather.datasources.local.room.models.RMWeather
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Weather(val id: Long, val main: String, val description: String, val icon: String?): Parcelable

fun Weather.toRMEntity(): RMWeather {
    return RMWeather(id = id, main, description, icon)
}