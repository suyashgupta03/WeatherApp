package com.weather.ui.services

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Binder
import android.os.IBinder
import android.os.Looper
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.*
import com.orhanobut.logger.Logger
import com.weather.R
import com.weather.entities.weather.LocationServiceResult
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Service tracks location when requested and updates Activity via binding. If Activity is
 * stopped/unbinds and tracking is enabled, the service promotes itself to a foreground service to
 * insure location updates aren't interrupted.
 *
 * For apps running in the background on O+ devices, location is computed much less than previous
 * versions. Please reference documentation for details.
 */
class ForegroundOnlyLocationService : Service() {
    /*
     * Checks whether the bound activity has really gone away (foreground service with notification
     * created) or simply orientation change (no-op).
     */
    private val localBinder = LocalBinder()

    private lateinit var notificationManager: NotificationManager

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var locationRequest: LocationRequest

    private lateinit var locationCallback: LocationCallback

    private var geoCoder: Geocoder? = null

    override fun onCreate() {
        Logger.d(TAG, "onCreate()")

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        geoCoder = Geocoder(this, Locale.getDefault())
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest().apply {
            // Sets the desired interval for active location updates. This interval is inexact. You
            // may not receive updates at all if no location sources are available, or you may
            // receive them less frequently than requested. You may also receive updates more
            // frequently than requested if other applications are requesting location at a more
            // frequent interval.
            interval = TimeUnit.SECONDS.toMillis(60)

            // Sets the fastest rate for active location updates. This interval is exact, and your
            // application will never receive updates more frequently than this value.
            fastestInterval = TimeUnit.SECONDS.toMillis(30)

            // Sets the maximum time when batched location updates are delivered. Updates may be
            // delivered sooner than this interval.
            maxWaitTime = TimeUnit.MINUTES.toMillis(2)

            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                if (locationResult?.lastLocation != null) {
                    val intent = Intent(ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST)
                    val data = LocationServiceResult(
                        true,
                        getCompleteAddressString(
                            locationResult.lastLocation.latitude,
                            locationResult.lastLocation.longitude
                        )
                    )
                    intent.putExtra(EXTRA_LOCATION, data)
                    LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                } else {
                    Logger.e(TAG, "Location missing in callback.")
                    sendErrorBroadcast(getString(R.string.err_fetching_location))
                }
            }
        }
    }

    private fun sendErrorBroadcast(message: String) {
        val intent = Intent(ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST)
        val data = LocationServiceResult(true, message)
        intent.putExtra(EXTRA_LOCATION, data)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }

    private fun getCompleteAddressString(lat: Double, lon: Double): String {
        var cityAddress = ""
        try {
            val addresses: List<Address>? = geoCoder?.getFromLocation(lat, lon, 1)
            if (addresses != null) {
                val returnedAddress: Address = addresses[0]
                val strReturnedAddress = StringBuilder("")
                strReturnedAddress.append(returnedAddress.locality).append(",")
                    .append(returnedAddress.countryName)
                cityAddress = strReturnedAddress.toString()
            } else {
                Logger.e("Address is returned null")
                sendErrorBroadcast(getString(R.string.err_fetching_location))
            }
        } catch (e: Exception) {
            Logger.e(e.localizedMessage ?: "")
            sendErrorBroadcast(getString(R.string.err_fetching_location))
        }
        return cityAddress
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Logger.d(TAG, "onStartCommand()")
        // Tells the system not to recreate the service after it's been killed.
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        Logger.d(TAG, "onBind()")
        return localBinder
    }

    override fun onUnbind(intent: Intent): Boolean {
        Logger.d(TAG, "onUnbind()")
        return true
    }

    override fun onDestroy() {
        Logger.d(TAG, "onDestroy()")
    }

    fun subscribeToLocationUpdates() {
        Logger.d(TAG, "subscribeToLocationUpdates()")
        // Binding to this service doesn't actually trigger onStartCommand(). That is needed to
        // ensure this Service can be promoted to a foreground service, i.e., the service needs to
        // be officially started (which we do here).
        startService(Intent(applicationContext, ForegroundOnlyLocationService::class.java))

        try {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest, locationCallback, Looper.myLooper()
            )
        } catch (unlikely: SecurityException) {
            Logger.e(TAG, "Lost location permissions. Couldn't remove updates. $unlikely")
            sendErrorBroadcast(getString(R.string.err_no_location_service))
        }
    }

    /**
     * Class used for the client Binder.  Since this service runs in the same process as its
     * clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {
        internal val service: ForegroundOnlyLocationService
            get() = this@ForegroundOnlyLocationService
    }

    companion object {
        private const val TAG = "ForegroundOnlyLocationService"

        private const val PACKAGE_NAME = "com.weather.location"

        internal const val ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST =
            "$PACKAGE_NAME.action.FOREGROUND_ONLY_LOCATION_BROADCAST"

        internal const val EXTRA_LOCATION = "$PACKAGE_NAME.extra.LOCATION"

    }
}
