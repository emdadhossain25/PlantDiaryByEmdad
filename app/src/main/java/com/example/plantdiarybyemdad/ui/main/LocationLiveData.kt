package com.example.plantdiarybyemdad.ui.main

import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData
import com.example.plantdiarybyemdad.dto.LocationDetails
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class LocationLiveData(context: Context) : LiveData<LocationDetails>() {

    private var fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    override fun onInactive() {
        super.onInactive()
    }

    override fun onActive() {
        super.onActive()
        startLocationUpdates()
    }


    private fun startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }


    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            locationResult ?: return
            for (location in locationResult.locations){
                setLocationData(location)
            }
        }
    }

    /**
     * if we received a location update this function will be called
     */
    private fun setLocationData(location: Location) {
        value= LocationDetails(location.longitude.toString(),location.latitude.toString())
    }

    companion object {
        val locationRequest: LocationRequest = LocationRequest.create().apply {
            val ONE_MINUTE: Long = 60000
            interval = ONE_MINUTE
            fastestInterval = ONE_MINUTE / 4
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

}
