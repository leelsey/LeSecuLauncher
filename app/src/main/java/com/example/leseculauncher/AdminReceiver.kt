package com.example.leseculauncher

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AdminReceiver : DeviceAdminReceiver() {

    override fun onEnabled(context: Context, intent: Intent) {
        Toast.makeText(context, "LeSecuLauncher Device Admin Enabled", Toast.LENGTH_SHORT).show()
    }

    override fun onDisabled(context: Context, intent: Intent) {
        Toast.makeText(context, "LeSecuLauncher Device Admin Disabled", Toast.LENGTH_SHORT).show()
    }

    // 강제 종료되거나 비정상적인 상황을 감지할 때 다시 앱을 시작하는 로직
    override fun onLockTaskModeExiting(context: Context, intent: Intent) {
        val restartIntent = context.packageManager.getLaunchIntentForPackage("com.example.leseculauncher")
        restartIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(restartIntent)
    }
}
