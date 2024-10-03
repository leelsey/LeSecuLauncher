package com.example.leseculauncher

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.leseculauncher.gps.GpsData
import com.example.leseculauncher.monitor.MonitorService

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            Toast.makeText(context, "LeSecuLauncher 시작", Toast.LENGTH_SHORT).show()
            // GpsService 및 MonitorService 시작
            GpsData.startGpsService(context)
            MonitorService.startMonitorService(context)
        }
    }
}
