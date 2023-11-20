package ru.kpfu.itis.galeev.android.myapplication

import android.app.AlertDialog
import android.app.Dialog
import android.app.Notification
import android.app.NotificationChannel
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.core.view.iterator
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationBarView
import ru.kpfu.itis.galeev.android.myapplication.base.BaseActivity
import ru.kpfu.itis.galeev.android.myapplication.databinding.ActivityMainBinding
import ru.kpfu.itis.galeev.android.myapplication.utils.RequestPermissionHandler

class MainActivity : BaseActivity() {
    override val fragmentContainerId = R.id.main_activity_container

    var _viewBinding : ActivityMainBinding? = null
    val viewBinding : ActivityMainBinding
        get() = _viewBinding!!

    var titles : HashMap<Int, String> = HashMap()

    var requestPermissionHandler : RequestPermissionHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        with(viewBinding) {
            fillTitles()
            val navHost = supportFragmentManager.findFragmentById(R.id.main_activity_container) as NavHostFragment
            val navController = navHost.navController

//            bnvBottomNavigation.setOnItemSelectedListener(object : NavigationBarView.OnItemSelectedListener {
//                override fun onNavigationItemSelected(item: MenuItem): Boolean {
//                    tvToolbarTitle.text = item.title
//                    return true
//                }
//            })
            changeTitleBar()

            bnvBottomNavigation.setupWithNavController(navController)
            requestPermissionHandler = RequestPermissionHandler(this@MainActivity)
            requestPermission()

            println("TEST TAG - ${NotificationChannel.DEFAULT_CHANNEL_ID}")
        }
    }

    fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionHandler?.requestPermission(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    fun fillTitles() {
        with(viewBinding) {
            val menu = bnvBottomNavigation.menu
            for (item in menu) {
                titles[item.itemId] = when (item.itemId) {
                    R.id.notificationsFragment -> resources.getString(R.string.notification_title)
                    R.id.settingsFragment -> resources.getString(R.string.settings_title)
                    R.id.coroutinesFragment -> resources.getString(R.string.title_coroutines)
                    else -> ""
                }
            }
        }
    }

    override fun changeTitleBar() {
        with(viewBinding) {
            tvToolbarTitle.text = titles[bnvBottomNavigation.selectedItemId]
        }
    }

    override fun onDestroy() {
        _viewBinding = null
        println("onDestroyCalled")
        super.onDestroy()
    }

    companion object {
        val CODE_NOTIFICATION_POST = 1024
    }
}