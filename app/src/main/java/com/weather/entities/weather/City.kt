package com.weather.entities.weather

import com.weather.datasources.local.room.models.RMCity

data class City(val id: Int, val name: String, val location: LatLon, val country: String)

fun City.toRMEntity(): RMCity {
    return RMCity(id, name, location.lat, location.lon, country)
}
