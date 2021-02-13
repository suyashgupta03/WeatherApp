package com.weather.entities.weather

import com.weather.datasources.local.room.models.RMWeatherForecast

data class WeatherForecast(val city: City, val cod: Int, val message: Float,
                           val count:Int, val weatherDay: List<WeatherDay>) {
    var id: Long = 0
}

fun WeatherForecast.toRMEntity(): RMWeatherForecast {
    return RMWeatherForecast(id = id, cityId = city.id, cod = cod, message = message, count = count)
}