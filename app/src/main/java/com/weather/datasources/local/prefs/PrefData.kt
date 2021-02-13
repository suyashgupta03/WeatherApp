package com.weather.datasources.local.prefs

interface PrefData {
    fun getCachedCityName(): String
    fun saveCachedCityName(cityName:String)
}