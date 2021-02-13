package com.weather.ui.fragments

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.snackbar.Snackbar
import com.orhanobut.logger.Logger
import com.weather.BuildConfig
import com.weather.R
import com.weather.databinding.FragmentSelectLocationBinding
import com.weather.datasources.local.prefs.PrefData
import com.weather.interactor.models.NavigationTarget
import com.weather.presentation.weather.SelectLocationViewModel
import com.weather.ui.common.BaseFragment
import com.weather.ui.services.ForegroundOnlyLocationService
import com.weather.entities.weather.LocationServiceResult
import com.weather.util.GpsUtils
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectLocationFragment :
    BaseFragment<SelectLocationViewModel, FragmentSelectLocationBinding>(),
    GpsUtils.OnGpsStatusChangeListener {

    override val viewModel: SelectLocationViewModel by viewModel()
    private val prefData: PrefData by inject()

    private var foregroundOnlyLocationServiceBound = false
    private var gpsUtils:GpsUtils? = null
    // Provides location updates for while-in-use feature.
    private var foregroundOnlyLocationService: ForegroundOnlyLocationService? = null

    // Listens for location broadcasts from ForegroundOnlyLocationService.
    private lateinit var foregroundOnlyBroadcastReceiver: ForegroundOnlyBroadcastReceiver

    // Used in checking for runtime permissions.
    private val codeRequestPermissionLocation = 34

    private lateinit var binding: FragmentSelectLocationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        foregroundOnlyBroadcastReceiver = ForegroundOnlyBroadcastReceiver()
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSelectLocationBinding {
        return FragmentSelectLocationBinding.inflate(inflater, container, false)
    }

    override fun doBindings(binding: FragmentSelectLocationBinding, savedInstanceState: Bundle?) {
        super.doBindings(binding, savedInstanceState)
        this.binding = binding
        initBindings()
    }

    private fun initBindings() {
        binding.btnLocationGPS.setOnClickListener {
            if (!checkPermissions()) {
                requestForegroundPermissions()
            } else if (!isLocationEnabled()) {
                activity?.let { activityNotNull ->
                    gpsUtils = GpsUtils(activityNotNull)
                    gpsUtils?.turnGPSOn(this)
                }
            } else {
                binding.btnLocationGPS.isEnabled = false
                binding.loadingContainer.loadingIndicator.visibility = VISIBLE
                foregroundOnlyLocationService?.subscribeToLocationUpdates()
                    ?: Logger.e("Service Not Bound")
            }
        }
        binding.btnLocationOk.setOnClickListener {
            val location = binding.etLocation.text.toString()
            if(location.isEmpty()) {
                Snackbar.make(it,
                    getString(R.string.err_empty_location_box),
                    Snackbar.LENGTH_LONG
                ).show()
            } else {
                prefData.saveCachedCityName(location)
                navigationHandler.navigateTo(NavigationTarget.ForecastScreen)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        activity?.let {
            val serviceIntent = Intent(it, ForegroundOnlyLocationService::class.java)
            it.bindService(serviceIntent, foregroundOnlyServiceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(
                foregroundOnlyBroadcastReceiver,
                IntentFilter(
                    ForegroundOnlyLocationService.ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST)
            )
        }
    }

    override fun onPause() {
        activity?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(
                foregroundOnlyBroadcastReceiver
            )
        }
        super.onPause()
    }

    override fun onStop() {
        if (foregroundOnlyLocationServiceBound) {
            activity?.unbindService(foregroundOnlyServiceConnection)
            foregroundOnlyLocationServiceBound = false
        }
        super.onStop()
    }

    // Monitors connection to the while-in-use service.
    private val foregroundOnlyServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as ForegroundOnlyLocationService.LocalBinder
            foregroundOnlyLocationService = binder.service
            foregroundOnlyLocationServiceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            foregroundOnlyLocationService = null
            foregroundOnlyLocationServiceBound = false
        }
    }

    /**
     * Returns the current state of the permissions needed.
     */
    private fun checkPermissions(): Boolean {
        context?.let { contextNotNull ->
            return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
                contextNotNull,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
        return false
    }

    private fun requestForegroundPermissions() {
        val provideRationale = checkPermissions()
        // If the user denied a previous request, but didn't check "Don't ask again", provide
        // additional rationale.
        if (provideRationale) {
            view?.let {
                Snackbar.make(
                    it,
                    R.string.location_permission_rationale,
                    Snackbar.LENGTH_LONG
                ).setAction(R.string.select_location_btn_ok) {
                        // Request permission
                        ActivityCompat.requestPermissions(
                            requireActivity(),
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            codeRequestPermissionLocation
                        )
                    }
                    .show()
            }
        } else {
            Logger.e("Request foreground only permission")
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                codeRequestPermissionLocation
            )
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        Logger.i("onRequestPermissionResult")
        if (requestCode == codeRequestPermissionLocation) {
            when {
                grantResults.isEmpty() -> {
                    // If user interaction was interrupted, the permission request is cancelled and you
                    // receive empty arrays.
                    Logger.i("User interaction was cancelled.")
                }
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    // Permission was granted.
                    foregroundOnlyLocationService?.subscribeToLocationUpdates()
                }
                else -> {
                    // Permission denied.
                    binding.btnLocationGPS.isEnabled = true
                    binding.loadingContainer.loadingIndicator.visibility = GONE
                    view?.let { viewNotNull ->
                        Snackbar.make(
                            viewNotNull,
                            R.string.location_permission_denied,
                            Snackbar.LENGTH_INDEFINITE
                        ).setAction(
                            R.string.settings
                        ) { // Build intent that displays the App settings screen.
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts(
                                "package",
                                BuildConfig.APPLICATION_ID, null
                            )
                            intent.data = uri
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }.show()
                    }
                }
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        context?.let {
            val lm = it.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || lm.isProviderEnabled(
                LocationManager.GPS_PROVIDER)
        }
        return false
    }

    /**
     * Receiver for location broadcasts from [ForegroundOnlyLocationService].
     */
    private inner class ForegroundOnlyBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val location = intent.getParcelableExtra<LocationServiceResult>(
                ForegroundOnlyLocationService.EXTRA_LOCATION
            )
            location?.let {
                if(it.isSuccess) {
                    prefData.saveCachedCityName(it.message)
                    navigationHandler.navigateTo(NavigationTarget.ForecastScreen)
                } else {
                    binding.loadingContainer.loadingErrorText.visibility = VISIBLE
                    binding.loadingContainer.loadingErrorText.text = it.message
                    view?.let { viewNotNull ->
                        Snackbar.make(viewNotNull,
                            it.message,
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }
            binding.btnLocationGPS.isEnabled = true
            binding.loadingContainer.loadingIndicator.visibility = GONE
        }
    }

    /**
     * when gps is enabled then the callback is called and we can proceed with location fetching
     */
    override fun gpsEnabled() {
        if (checkPermissions()) {
            foregroundOnlyLocationService?.subscribeToLocationUpdates()
                ?: Logger.e("Service Not Bound")
        } else {
            requestForegroundPermissions()
        }
    }

    override fun gpsError(message: String) {
        view?.let {
            Snackbar.make(it,
                message,
                Snackbar.LENGTH_LONG
            ).show()
        }
    }
}