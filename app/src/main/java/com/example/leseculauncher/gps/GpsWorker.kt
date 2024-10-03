package com.example.leseculauncher.gps

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*

class GpsWorker(private val context: Context) {

    private val locationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 60000).apply {
        setMinUpdateDistanceMeters(500f)
        setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
        setWaitForAccurateLocation(true)
    }.build()

    fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        locationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                GpsData.lat = location.latitude
                GpsData.lon = location.longitude
                GpsData.accuracy = location.accuracy.toDouble()
                GpsData.lastUpdatedTime = System.currentTimeMillis()
            }
        }

        locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            if (locationResult.locations.isNotEmpty()) {
                val location = locationResult.locations[0]
                GpsData.lat = location.latitude
                GpsData.lon = location.longitude
                GpsData.lastUpdatedTime = System.currentTimeMillis()
            }
        }
    }

    fun stopLocationUpdates() {
        locationProviderClient.removeLocationUpdates(locationCallback)
    }
}
