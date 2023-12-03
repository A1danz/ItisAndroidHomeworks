package ru.kpfu.itis.galeev.android.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.view.iterator
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ru.kpfu.itis.galeev.android.myapplication.base.BaseActivity
import ru.kpfu.itis.galeev.android.myapplication.databinding.ActivityMainBinding
import ru.kpfu.itis.galeev.android.myapplication.utils.ParamsKey

class MainActivity : BaseActivity() {
    override val fragmentContainerId = R.id.main_activity_container

    var _viewBinding : ActivityMainBinding? = null
    val viewBinding : ActivityMainBinding
        get() = _viewBinding!!

    var titles : HashMap<Int, String> = HashMap()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent : Intent = intent

        _viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        with(viewBinding) {
            val navHost = supportFragmentManager.findFragmentById(R.id.main_activity_container) as NavHostFragment
            val navController = navHost.navController
            }
        }

    override fun changeTitleBar() {
        with(viewBinding) {
            tvToolbarTitle.text = titles[bnvBottomNavigation.selectedItemId]
        }
    }

    override fun onDestroy() {
        _viewBinding = null
        super.onDestroy()
    }
}