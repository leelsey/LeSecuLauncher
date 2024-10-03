package com.example.leseculauncher.gps

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.leseculauncher.MainActivity
import com.example.leseculauncher.R

class GpsService : Service() {

    companion object {
        const val NOTIFICATION_ID = 1
        const val channelId = "GPS_Service"
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, createChannel().build())
        GpsData.startLocationUpdates()
        return START_STICKY
    }

    private fun createChannel(): NotificationCompat.Builder {
        if (Build.VERSION_CODES.O <= Build.VERSION.SDK_INT) {
            val channel = NotificationChannel(
                channelId, "Service Channel", NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("LeSecuLauncher Service")
            .setContentText("GPS 서비스가 실행 중입니다.")
            .setContentIntent(pendingIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        GpsData.stopLocationUpdates()
    }
}
