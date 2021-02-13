package com.weather.datasources.api.models

import com.squareup.moshi.JsonClass
import com.weather.datasources.api.common.generateMappingError
import com.weather.entities.weather.LatLon

@JsonClass(generateAdapter = true)
data class ApiWeatherLatLon(
    val lon: Float?,
    val lat: Float?
)

fun ApiWeatherLatLon.toObjOrNull(): LatLon? {
    return when {
        lat == null -> generateMappingError("ApiWeatherLatLon", ::lat)
        lon == null -> generateMappingError("ApiWeatherLatLon", ::lon)
        else -> LatLon(lat, lon)
    }
}
