package ru.kpfu.itis.galeev.android.myapplication.utils

import android.app.AlertDialog
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ru.kpfu.itis.galeev.android.myapplication.base.BaseActivity

class RequestPermissionHandler(
    activity : BaseActivity,
) {
    private var permissionName : String = ""
    private val singlePermissionLauncher =
        activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) {isGranted ->
            if (isGranted == false) {
                when {
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        activity, android.Manifest.permission.POST_NOTIFICATIONS
                    ) -> {
                        val builder = AlertDialog.Builder(activity)
                        builder.setTitle("Разрешение на отправку уведомлений.")
                        builder.setMessage("Пожалуйста, дайте разрешение на отправку уведмолений.")
                            .setPositiveButton("Ok") { dialog, id ->
                                this.requestPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                            }
                        builder.create().show()
                    } else -> {
                        createUnclosedAlertDialog(activity)
                    }
                }
            }
        }

    private fun createUnclosedAlertDialog(activity : BaseActivity) {
        val builder = AlertDialog.Builder(activity)
        builder.setCancelable(false)
        builder.setTitle("Разрешение на отправку")
        builder.setMessage("Для дальнейшей корректной работы приложения необходимо" +
                " дать разрешение на отправку - перейдите в настройки.")
            .setPositiveButton("Ok") {dialog, id ->
            if (ContextCompat.checkSelfPermission(
                    activity,
                    android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_DENIED) {
                builder.create().show()
            }
        }
        builder.create().show()
    }
    fun requestPermission(permission : String) {
        permissionName = permission
        singlePermissionLauncher.launch(permission)

    }
}