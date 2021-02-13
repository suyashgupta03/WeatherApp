package com.weather.util

interface DateUtil {
    fun convertEpochToLocalDate(epoch: Long): String

    fun convertEpochToLocalTime(epoch: Long): String

    fun getTimeOfDay(): TimeOfDay
}