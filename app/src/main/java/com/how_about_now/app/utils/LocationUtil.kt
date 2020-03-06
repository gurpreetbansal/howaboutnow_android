package com.how_about_now.app.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.location.*
import com.how_about_now.app.R


class LocationUtil : ConnectionCallbacks, OnConnectionFailedListener, LocationListener,
    ResultCallback<LocationSettingsResult> {

    /**
     * Provides the entry point to Google Play services.
     */
    private var mGoogleApiClient: GoogleApiClient? = null

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private var mLocationRequest: LocationRequest? = null

    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    private var mLocationSettingsRequest: LocationSettingsRequest? = null

    /**
     * Represents a geographical location.
     */
    private var mCurrentLocation: Location? = null

    private var activity: Activity? = null
    private var locationUpdateListener: LocationUpdateListener? = null
    private var isInfiniteGetCurrentLocation = false

    private fun checkLocationInstance(activity: Activity, locationUpdateListener: LocationUpdateListener) {
        if (mGoogleApiClient != null && mGoogleApiClient!!.isConnected) {
            this.locationUpdateListener = locationUpdateListener
            if (!isInfiniteGetCurrentLocation) {
                checkLocationSettings()
            } else
                onConnected(null)
        } else
            buildLocationRequest(activity, locationUpdateListener)
    }

    fun doContinuousLocation(isInfiniteGetCurrentLocation: Boolean): LocationUtil {
        this.isInfiniteGetCurrentLocation = isInfiniteGetCurrentLocation
        return this
    }

    private fun buildLocationRequest(activity: Activity, locationUpdateListener: LocationUpdateListener) {
        this.locationUpdateListener = locationUpdateListener
        this.activity = activity
        buildGoogleApiClient()
        createLocationRequest()
        buildLocationSettingsRequest()
        checkLocationSettings()
    }

    /**
     * Builds a GoogleApiClient. Uses the `#addApi` method to request the
     * LocationServices API.
     */
    @Synchronized
    private fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(activity!!)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        mGoogleApiClient!!.connect()
    }

    private fun createLocationRequest() {
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest!!.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
        //  mLocationRequest.setMaxWaitTime(10000);
        mLocationRequest!!.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        mLocationRequest!!.smallestDisplacement = 10f
    }

    /**
     * Uses a [com.google.android.gms.location.LocationSettingsRequest.Builder] to build
     * a [com.google.android.gms.location.LocationSettingsRequest] that is used for checking
     * if a device has the needed location settings.
     */
    private fun buildLocationSettingsRequest() {
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
        mLocationSettingsRequest = builder.build()
    }

    /**
     * Check if the device's location settings are adequate for the app's needs using the
     * [com.google.android.gms.location.SettingsApi.checkLocationSettings] method, with the results provided through a `PendingResult`.
     */
    private fun checkLocationSettings() {
        val result = LocationServices.SettingsApi.checkLocationSettings(
            mGoogleApiClient,
            mLocationSettingsRequest
        )
        result.setResultCallback(this)
    }

    override fun onResult(@NonNull locationSettingsResult: LocationSettingsResult) {
        val status = locationSettingsResult.status
        when (status.statusCode) {
            LocationSettingsStatusCodes.SUCCESS ->
                //  startLocationUpdates();
                if (mGoogleApiClient != null && mGoogleApiClient!!.isConnected)
                    startLocationUpdates()
                else
                    buildGoogleApiClient()
            LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                status.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS)
            } catch (ignored: IntentSender.SendIntentException) {
            }

            LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                Log.e(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " + "not created.")
                locationUpdateListener!!.OnLocationError("Location settings are inadequate, and cannot be fixed here. Dialog " + "not created.")
            }
        }
    }

    private fun setResults(requestCode: Int, resultCode: Int) {
        when (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            REQUEST_CHECK_SETTINGS -> when (resultCode) {
                Activity.RESULT_OK -> startLocationUpdates()
                Activity.RESULT_CANCELED -> locationUpdateListener!!.OnLocationError("Canceled by User")
            }// activity.finish();
        }
    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            locationUpdateListener!!.OnLocationError("You have to do grant permission of Location")
            return
        }
        if (mGoogleApiClient!!.isConnected) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                mLocationRequest,
                this
            ).setResultCallback { }
        }
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    private fun stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
            mGoogleApiClient,
            this
        ).setResultCallback { status -> Log.e("---Location Stop--- ", status.toString() + "") }
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @SuppressLint("MissingPermission")
    override fun onConnected(connectionHint: Bundle?) {
        if (ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(
                activity,
                "You have to do grant permission of ACCESS_FINE_LOCATION and ACCESS_COARSE_LOCATION.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)
        if (mCurrentLocation != null)
            locationChangedFired(mCurrentLocation!!)
        else
            startLocationUpdates()
    }

    /**
     * Callback that fires when the location changes.
     */
    override fun onLocationChanged(location: Location) {
        Log.e(TAG, "---onLocationChanged--- " + location.latitude + ", " + location.longitude)
        locationChangedFired(location)
    }

    private fun locationChangedFired(location: Location) {
        if (!isInfiniteGetCurrentLocation && mGoogleApiClient != null && mGoogleApiClient!!.isConnected) {
            stopLocationListners()
        }
        if (locationUpdateListener != null) {
            Log.e(TAG, "---location callback fire--")
            locationUpdateListener!!.onLocationChanged(location)
            locationUpdateListener = null
        }

    }

    fun stopLocationListners() {
        Log.e(TAG, "---location listeners--- stop")
        stopLocationUpdates()
        mGoogleApiClient!!.disconnect()
    }

    override fun onConnectionSuspended(cause: Int) {
        Log.i(TAG, "Connection suspended")
    }

    override fun onConnectionFailed(@NonNull result: ConnectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.errorCode)
    }

    private fun buildAlertMessageNoGps(context: Context) {
        val builder = AlertDialog.Builder(context, R.style.DialogStyle)
        builder.setMessage("Your GPS seems to be disabled, please enable the gps after that you can use dating app.")
            .setCancelable(false)
            .setPositiveButton("Ok") { dialog, id -> context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
        val alert = builder.create()
        alert.show()
    }

    interface LocationUpdateListener {
        fun onLocationChanged(location: Location)

        fun OnLocationError(error: String)
    }

    companion object {

        private val TAG = "LocationUtil"

        /**
         * Constant used in the location settings dialog.
         */
        private val REQUEST_CHECK_SETTINGS = 0x1

        /**
         * The desired interval for location updates. Inexact. Updates may be more or less frequent.
         */
        private val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 60000

        /**
         * The fastest rate for active location updates. Exact. Updates will never be more frequent
         * than this value.
         */
        private val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS: Long = 1000

        private var locationUtil: LocationUtil? = null


        fun with(activity: Activity, locationUpdateListener: LocationUpdateListener): LocationUtil {
            if (locationUtil == null)
                locationUtil = LocationUtil()
            locationUtil!!.checkLocationInstance(activity, locationUpdateListener)
            return locationUtil!!
        }

        fun onActivityResult(requestCode: Int, resultCode: Int) {
            if (locationUtil != null)
                locationUtil!!.setResults(requestCode, resultCode)
        }

        private fun isGPSEnabled(context: Context): Boolean {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        }

        internal fun getAddress(lat: Double, lang: Double, context: Context): String? {
            try {
                val geocoder: Geocoder
                val addresses: List<Address>
                geocoder = Geocoder(context)
                if (lat != 0.0 || lang != 0.0) {

                    addresses = geocoder.getFromLocation(lat, lang, 1)
                    val address = addresses[0].getAddressLine(0)
                    val city = addresses[0].getAddressLine(1)
                    val country = addresses[0].getAddressLine(2)
                    val state = addresses[0].subLocality
                    return address + ", " + city + ", " + (country ?: "")
                } else {
                    return null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }

        }
    }
}
