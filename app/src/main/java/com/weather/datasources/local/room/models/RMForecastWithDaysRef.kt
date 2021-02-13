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
        entity = RMWeatherForecast::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class RMForecastWithDaysRef(val id: Long, val date: Long)