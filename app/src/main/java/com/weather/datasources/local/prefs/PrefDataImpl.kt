package com.weather.datasources.local.prefs

class PrefDataImpl(private val sharedPreference: SharedPreference) : PrefData {

    override fun getCachedCityName(): String {
        return sharedPreference.getValueString(KEY_CITY_NAME)
    }

    override fun saveCachedCityName(cityName: String) {
        sharedPreference.save(KEY_CITY_NAME, cityName)
    }

    companion object {
        private const val KEY_CITY_NAME = "keyCityName"
    }
}
