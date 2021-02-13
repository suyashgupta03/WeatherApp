package com.weather.datasources.api.models

import com.squareup.moshi.JsonClass
import com.weather.datasources.api.common.generateMappingError
import com.weather.entities.weather.WeatherForecast

@JsonClass(generateAdapter = true)
data class ApiWeatherForecast(
    val city: ApiWeatherCity?,
    val cod: Int?,
    val message: Float?,
    val cnt: Int?,
    val list: List<ApiWeatherDay>?
)

fun ApiWeatherForecast.toObjOrNull(): WeatherForecast? {
    val cityLocal = city?.toObjOrNull()
    return when {
        cityLocal == null -> generateMappingError("ApiWeatherForecast", ::city)
        cod == null -> generateMappingError("ApiWeatherForecast", ::cod)
        message == null -> generateMappingError("ApiWeatherForecast", ::message)
        cnt == null -> generateMappingError("ApiWeatherForecast", ::cnt)
        list.isNullOrEmpty() -> generateMappingError("ApiWeatherForecast", ::list)
        else -> WeatherForecast(cityLocal, cod, message, cnt, list.mapNotNull { it.toObjOrNull() })
    }
}
