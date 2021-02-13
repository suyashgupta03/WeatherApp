package com.weather.datasources.api.models

import com.squareup.moshi.JsonClass
import com.weather.datasources.api.common.generateMappingError
import com.weather.entities.weather.City

@JsonClass(generateAdapter = true)
data class ApiWeatherCity(
    val id: Int?,
    val name: String?,
    val coord: ApiWeatherLatLon?,
    val country: String?,
    val population: Long?,
    val timezone: Int?
)

fun ApiWeatherCity.toObjOrNull(): City? {
    val location = coord?.toObjOrNull()
    return when {
        id == null -> generateMappingError("ApiWeatherCity", ::id)
        name.isNullOrBlank() -> generateMappingError("ApiWeatherCity", ::name)
        location == null -> generateMappingError("ApiWeatherCity", ::coord)
        country.isNullOrBlank() -> generateMappingError("ApiWeatherCity", ::country)
        else -> City(id, name, location, country)
    }
}
