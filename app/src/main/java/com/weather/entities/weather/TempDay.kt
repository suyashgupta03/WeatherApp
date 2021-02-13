package com.weather.entities.weather

import android.os.Parcelable
import com.weather.datasources.local.room.models.RMTemp
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TempDay(
    val night: Float,
    val eve: Float,
    val day: Float,
    val morn: Float
) : Parcelable {
    var id: Long = 0
}

fun TempDay.toRMEntity(): RMTemp {
    return RMTemp(id = id, night = night, eve = eve, day = day, morn = morn)
}