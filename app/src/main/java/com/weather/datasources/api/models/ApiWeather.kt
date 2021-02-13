package com.weather.datasources.api.models

import com.squareup.moshi.JsonClass
import com.weather.datasources.api.common.generateMappingError
import com.weather.entities.weather.Weather

@JsonClass(generateAdapter = true)
data class ApiWeather(
    val id: Long?,
    val main: String?,
    val description: String?,
    val icon: String?
)

fun ApiWeather.toObjOrNull(): Weather? {
    return when {
        id == null -> generateMappingError("ApiWeather", ::id)
        main.isNullOrBlank() -> generateMappingError("ApiWeather", ::main)
        description.isNullOrBlank() -> generateMappingError("ApiWeather", ::description)
        else -> Weather(id, main, description, icon)
    }
}
