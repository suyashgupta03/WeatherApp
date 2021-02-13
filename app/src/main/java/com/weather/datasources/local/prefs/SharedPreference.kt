package com.weather.datasources.local.prefs

interface SharedPreference {
    fun save(key: String, text: String)
    fun getValueString(key: String): String
}