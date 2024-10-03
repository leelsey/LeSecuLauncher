package com.example.leseculauncher.monitor

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import com.example.leseculauncher.gps.GpsData

class MonitorService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!GpsData.isServiceRunning.value!!) {
            GpsData.startGpsService(this)
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        GpsData.startGpsService(this)
    }

    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun startMonitorService(context: Context) {
            val intent = Intent(context, MonitorService::class.java)
            context.startForegroundService(intent)
        }
    }
}
