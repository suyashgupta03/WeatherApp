package com.weather.util

import android.app.Activity
import android.content.Context
import android.content.IntentSender.SendIntentException
import android.location.LocationManager
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.orhanobut.logger.Logger
import com.weather.R

private const val CODE_REQ_GPS = 6345

class GpsUtils(private val activity: Activity) {
    private var mSettingsClient: SettingsClient? = null
    private var mLocationSettingsRequest: LocationSettingsRequest? = null
    private var locationManager: LocationManager? = null
    private var locationRequest: LocationRequest? = null

    init {
        locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        mSettingsClient = LocationServices.getSettingsClient(activity)
        locationRequest = LocationRequest.create()
        locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest?.interval = (10 * 1000).toLong()
        locationRequest?.fastestInterval = (2 * 1000).toLong()
        locationRequest?.let {
            val builder = LocationSettingsRequest.Builder().addLocationRequest(it)
            mLocationSettingsRequest = builder.build()
            builder.setAlwaysShow(true) //this is the key ingredient
        }
    }

    // method to turn on GPS
    fun turnGPSOn(listener: OnGpsStatusChangeListener) {
        if (locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true) {
            listener.gpsEnabled()
        } else {
            mSettingsClient?.checkLocationSettings(mLocationSettingsRequest)
                ?.addOnSuccessListener(activity) {
                    listener.gpsEnabled()
                }?.addOnFailureListener(activity) { e ->
                    when ((e as ApiException).statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                            // Show the dialog by calling startResolutionForResult(), and check the
                            // result in onActivityResult().
                            val rae = e as ResolvableApiException
                            rae.startResolutionForResult(
                                activity,
                                CODE_REQ_GPS
                            )
                        } catch (sie: SendIntentException) {
                            Logger.e("PendingIntent unable to execute request: "+ sie.localizedMessage)
                            listener.gpsError(sie.localizedMessage ?: "")
                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                            val errorMessage = activity.getString(R.string.err_no_location_service)
                            Logger.e(errorMessage)
                            listener.gpsError(errorMessage)
                        }
                    }
                }
        }
    }

    interface OnGpsStatusChangeListener {
        fun gpsEnabled()
        fun gpsError(message: String)
    }
}