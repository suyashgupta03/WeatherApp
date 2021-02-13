package com.weather.entities.weather

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocationServiceResult(val isSuccess:Boolean, val message:String) : Parcelable
