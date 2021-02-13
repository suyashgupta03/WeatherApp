package com.weather.datasources.local.room.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    primaryKeys = ["id", "date"],
    indices = [Index(value = ["id", "date"])],
    foreignKeys = [ForeignKey(
        entity = RMWeatherDay::class,
        parentColumns = arrayOf("date"),
        childColumns = arrayOf("date"),
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = RMWeather::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class RMDayWithWeathersRef(val date: Long, val id: Long)