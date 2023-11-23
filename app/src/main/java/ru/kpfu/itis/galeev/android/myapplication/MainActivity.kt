package ru.kpfu.itis.galeev.android.myapplication

import android.app.AlertDialog
import android.app.Dialog
import android.app.Notification
import android.app.NotificationChannel
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.core.view.iterator
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationBarView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.newCoroutineContext
import kotlinx.coroutines.withContext
import ru.kpfu.itis.galeev.android.myapplication.base.BaseActivity
import ru.kpfu.itis.galeev.android.myapplication.base.BaseFragment
import ru.kpfu.itis.galeev.android.myapplication.databinding.ActivityMainBinding
import ru.kpfu.itis.galeev.android.myapplication.fragments.AirplaneBtnsFragment
import ru.kpfu.itis.galeev.android.myapplication.utils.CoroutinesConfig
import ru.kpfu.itis.galeev.android.myapplication.utils.ParamsKey
import ru.kpfu.itis.galeev.android.myapplication.utils.RequestPermissionHandler

class MainActivity : BaseActivity() {
    override val fragmentContainerId = R.id.main_activity_container

    var _viewBinding : ActivityMainBinding? = null
    val viewBinding : ActivityMainBinding
        get() = _viewBinding!!

    var titles : HashMap<Int, String> = HashMap()

    private val receiver by lazy { getAirplaneModeBroadcastReceiver() }

    var requestPermissionHandler : RequestPermissionHandler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent : Intent = intent

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
            println("TEST TAG - HAS KEY ${intent.extras?.containsKey(ParamsKey.WITH_TOAST)}")
            if (intent.extras?.containsKey(ParamsKey.WITH_TOAST) == true) {
                Toast.makeText(this@MainActivity, "You open this by intent", Toast.LENGTH_SHORT).show()
            }
            println("TEST TAG - ${intent.extras?.containsKey(ParamsKey.OPEN_SETTINGS)}")
            if (intent.extras?.containsKey(ParamsKey.OPEN_SETTINGS) == true) {
                bnvBottomNavigation.selectedItemId = R.id.settingsFragment
            }
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

    override fun onResume() {
        super.onResume()

        changeActivityByAirplaneState(Settings.System.getInt(this.contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) == 1)
        registerReceiver(receiver, IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED))
    }

    override fun onPause() {
        super.onPause()

        unregisterReceiver(receiver)
    }

    private fun getAirplaneModeBroadcastReceiver() : BroadcastReceiver {
        return object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == Intent.ACTION_AIRPLANE_MODE_CHANGED) {
                    changeActivityByAirplaneState(intent.getBooleanExtra("state", false))
                }
            }
        }
    }


    fun changeActivityByAirplaneState(state : Boolean) {
        with(viewBinding) {
            cvAirplaneMode.isVisible = state
            val navHostFragment = supportFragmentManager.fragments.first() as? NavHostFragment
            navHostFragment?.let { fragment ->
                (fragment.childFragmentManager.fragments.last() as? AirplaneBtnsFragment)
                    ?.changeBtnByModeState(!state)
            }
        }
    }


    override fun onDestroy() {
        _viewBinding = null
        super.onDestroy()
    }
}