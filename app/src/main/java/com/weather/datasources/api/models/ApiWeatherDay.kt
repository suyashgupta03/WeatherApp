package com.weather.datasources.api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.weather.datasources.api.common.generateMappingError
import com.weather.entities.weather.WeatherDay

@JsonClass(generateAdapter = true)
data class ApiWeatherDay (
    @Json(name = "dt")
    val date: Long?,
    val sunrise: Long?,
    val sunset: Long?,
    val temp: ApiWeatherTemp?,
    val feels_like: ApiWeatherFeelsLike?,
    val pressure: Int?,
    val humidity: Int?,
    val weather: List<ApiWeather>?,
    val speed:Float?,
    val deg: Int?,
    val clouds: Int?,
    val pop: Float?,
    val snow: Float?
)

fun ApiWeatherDay.toObjOrNull(): WeatherDay? {
    val tempLocal = temp?.toObjOrNull()
    val min = temp?.min
    val max = temp?.max
    val feelsLike = feels_like?.toObjOrNull()
    return when {
        date == null -> generateMappingError("ApiWeatherForecast", ::date)
        sunrise == null -> generateMappingError("ApiWeatherForecast", ::sunrise)
        sunset == null -> generateMappingError("ApiWeatherForecast", ::sunset)
        tempLocal == null -> generateMappingError("ApiWeatherForecast", ::temp)
        min == null -> generateMappingError("ApiWeatherForecastMin", ::temp)
        max == null -> generateMappingError("ApiWeatherForecastMax", ::temp)
        feelsLike == null -> generateMappingError("ApiWeatherForecast", ::feels_like)
        pressure == null -> generateMappingError("ApiWeatherForecast", ::pressure)
        humidity == null -> generateMappingError("ApiWeatherForecast", ::humidity)
        weather.isNullOrEmpty() -> generateMappingError("ApiWeatherForecast", ::weather)
        speed == null -> generateMappingError("ApiWeatherForecast", ::speed)
        else -> WeatherDay(date, sunrise, sunset, tempLocal, min, max, feelsLike, pressure, humidity, weather.mapNotNull { it.toObjOrNull() }, speed, deg, clouds, pop, snow)
    }
}
