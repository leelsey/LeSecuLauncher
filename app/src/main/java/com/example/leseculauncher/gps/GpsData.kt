package com.example.leseculauncher.gps

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData

object GpsData {

    val isServiceRunning = MutableLiveData<Boolean>(false)
    var lat = 0.0
    var lon = 0.0
    var accuracy = 0.0
    var lastUpdatedTime: Long = 0

    fun startGpsService(context: Context) {
        if (!isServiceRunning.value!!) {
            val intent = Intent(context, GpsService::class.java)
            ContextCompat.startForegroundService(context, intent)
            Toast.makeText(context, "GPS 서비스 시작", Toast.LENGTH_SHORT).show()
            isServiceRunning.postValue(true)
        }
    }

    fun stopGpsService(context: Context) {
        val intent = Intent(context, GpsService::class.java)
        context.stopService(intent)
        Toast.makeText(context, "GPS 서비스 중지", Toast.LENGTH_SHORT).show()
        isServiceRunning.postValue(false)
    }

    fun startLocationUpdates() {
        // GPS 위치 업데이트 로직 (GpsWorker와 연동)
    }

    fun stopLocationUpdates() {
        // 위치 업데이트 중지 로직
    }
}
