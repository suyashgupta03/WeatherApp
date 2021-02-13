package com.weather.datasources.api.models

import com.squareup.moshi.JsonClass
import com.weather.datasources.api.common.generateMappingError
import com.weather.entities.weather.TempDay

@JsonClass(generateAdapter = true)
data class ApiWeatherTemp (
    val min: Float?,
    val max: Float?,
    val night: Float?,
    val eve: Float?,
    val day: Float?,
    val morn: Float?
)

fun ApiWeatherTemp.toObjOrNull(): TempDay? {
    return when {
        min == null -> generateMappingError("ApiWeatherTemp", ::min)
        max == null -> generateMappingError("ApiWeatherTemp", ::max)
        day == null -> generateMappingError("ApiWeatherTemp", ::day)
        eve == null -> generateMappingError("ApiWeatherTemp", ::eve)
        morn == null -> generateMappingError("ApiWeatherTemp", ::morn)
        night == null -> generateMappingError("ApiWeatherTemp", ::night)
        else -> TempDay(night, eve, day, morn)
    }
}
