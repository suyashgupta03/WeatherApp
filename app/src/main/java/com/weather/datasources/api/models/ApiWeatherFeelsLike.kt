package com.weather.datasources.api.models

import com.squareup.moshi.JsonClass
import com.weather.datasources.api.common.generateMappingError
import com.weather.entities.weather.TempDay

@JsonClass(generateAdapter = true)
data class ApiWeatherFeelsLike(
    val day: Float?,
    val night: Float?,
    val eve: Float?,
    val morn: Float?
)

fun ApiWeatherFeelsLike.toObjOrNull(): TempDay? {
    return when {
        day == null -> generateMappingError("ApiWeatherFeelsLike", ::day)
        eve == null -> generateMappingError("ApiWeatherFeelsLike", ::eve)
        morn == null -> generateMappingError("ApiWeatherFeelsLike", ::morn)
        night == null -> generateMappingError("ApiWeatherFeelsLike", ::night)
        else -> TempDay(night, eve, day, morn)
    }
}
