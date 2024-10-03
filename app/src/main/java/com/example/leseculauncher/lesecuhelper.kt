package com.example.lesecuhelper

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class HelperService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // leseculauncher 감시 로직
        val launcherPackage = "com.example.leseculauncher"
        if (!isAppRunning(launcherPackage)) {
            val restartIntent = packageManager.getLaunchIntentForPackage(launcherPackage)
            restartIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(restartIntent)
            Log.i("HelperService", "leseculauncher 앱을 다시 시작했습니다.")
        }
        return START_STICKY
    }

    private fun isAppRunning(packageName: String): Boolean {
        // 앱이 실행 중인지 확인하는 로직
        val activityManager = getSystemService(ACTIVITY_SERVICE) as android.app.ActivityManager
        val runningApps = activityManager.runningAppProcesses ?: return false
        return runningApps.any { it.processName == packageName }
    }
}
