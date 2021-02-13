package com.weather.datasources.local.prefs

import android.content.Context
import android.content.SharedPreferences

private const val NAME_PREFS = "prefs_weather"
private const val EMPTY = ""

class SharedPreferenceImpl(context: Context) : SharedPreference {

    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(NAME_PREFS, Context.MODE_PRIVATE)

    override fun save(key: String, text: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(key, text)
        editor.apply()
    }

    override fun getValueString(key: String): String {
        return sharedPref.getString(key, EMPTY) ?: EMPTY
    }
}