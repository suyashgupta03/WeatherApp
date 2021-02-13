package com.weather.util

import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.*

class DateUtilImpl : DateUtil {

    override fun convertEpochToLocalDate(epoch: Long): String {
        if (epoch < 0) return ""
        val formatter = SimpleDateFormat(LOCAL_DATE_FORMAT, Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone(ZoneId.systemDefault())
        return formatter.format(Date(epoch * 1000))
    }

    override fun convertEpochToLocalTime(epoch: Long): String {
        if (epoch < 0) return ""
        val formatter = SimpleDateFormat(LOCAL_TIME_FORMAT, Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone(ZoneId.systemDefault())
        return formatter.format(Date(epoch * 1000))
    }

    override fun getTimeOfDay(): TimeOfDay {
        val c = Calendar.getInstance()
        return when (c[Calendar.HOUR_OF_DAY]) {
            in 0..11 -> TimeOfDay.MORNING
            in 12..15 -> TimeOfDay.AFTERNOON
            in 16..20 -> TimeOfDay.EVENING
            in 21..23 -> TimeOfDay.NIGHT
            else -> TimeOfDay.MORNING
        }
    }

    companion object {
        const val LOCAL_DATE_FORMAT = "dd/MM/yy"
        const val LOCAL_TIME_FORMAT = "dd/MM/yy 'at' HH:mm:ss"
    }
}

enum class TimeOfDay{ MORNING, EVENING, NIGHT, AFTERNOON}