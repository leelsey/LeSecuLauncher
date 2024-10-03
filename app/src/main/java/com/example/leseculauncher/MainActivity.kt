package com.example.leseculauncher

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.example.leseculauncher.databinding.ActivityMainBinding
import com.example.leseculauncher.gps.GpsData
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val isServiceRunning = Observer<Boolean> { isRunning ->
        if (!isRunning) {
            Timber.e("service 상태 변화 감지")
            GpsData.startGpsService(this@MainActivity)
            GpsData.isServiceRunning.postValue(true) // 서비스 실행 상태 업데이트
        }
    }

    val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.POST_NOTIFICATIONS
    )

    private var isAllPermissionGrant: Boolean = false
    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionsMap ->
            if (permissions.all { permissionsMap[it] == true }) {
                isAllPermissionGrant = true
                GpsData.isServiceRunning.observe(this, isServiceRunning)
                GpsData.startGpsService(this@MainActivity)
            } else {
                val deniedPermissions = permissions.filter { permissionsMap[it] != true }
                if (deniedPermissions.any { shouldShowRequestPermissionRationale(it) }) {
                    showRationaleDialog(deniedPermissions.toTypedArray())
                } else {
                    showSettingsDialog()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.apply {
            mainActivity = this@MainActivity // XML에서 mainActivity 변수로 접근할 수 있도록 설정
        }

        // 권한 요청
        requestPermissionsLauncher.launch(permissions)
    }

    fun stopService() {
        GpsData.stopGpsService(this@MainActivity)
        Toast.makeText(this@MainActivity, "Service Stop", Toast.LENGTH_SHORT).show()
    }

    private fun showRationaleDialog(deniedPermissions: Array<String>) {
        AlertDialog.Builder(this)
            .setMessage("이 기능을 사용하기 위해 권한이 필요합니다.")
            .setPositiveButton("다시 요청") { _, _ ->
                requestPermissionsLauncher.launch(deniedPermissions)
            }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun showSettingsDialog() {
        AlertDialog.Builder(this)
            .setMessage("권한을 활성화해야 이 기능을 사용할 수 있습니다. 설정 화면으로 이동하시겠습니까?")
            .setPositiveButton("설정으로 이동") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("취소", null)
            .show()
    }
}
