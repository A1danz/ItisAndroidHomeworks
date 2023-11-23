package ru.kpfu.itis.galeev.android.myapplication.utils

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ru.kpfu.itis.galeev.android.myapplication.R
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
                        builder.setTitle(ContextCompat.getString(activity, R.string.notification_dialog_title))
                        builder.setMessage(ContextCompat.getString(activity, R.string.notification_rationale_text))
                            .setPositiveButton(ContextCompat.getString(activity, R.string.btn_ok)) { dialog, id ->
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
        builder.setTitle(ContextCompat.getString(activity, R.string.notification_dialog_title))
        builder.setMessage(ContextCompat.getString(activity, R.string.notification_after_rationale_text))
            .setPositiveButton(ContextCompat.getString(activity, R.string.btn_settings)) { dialog, id ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.setData(Uri.parse("package:" + activity.packageName))
                activity.startActivity(intent)
            }
            .setNegativeButton(
                ContextCompat.getString(activity, R.string.btn_cancel)
            ) { dialog, id ->
                if (ContextCompat.checkSelfPermission(
                        activity,
                        android.Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_DENIED
                ) {
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