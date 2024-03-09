package ru.kpfu.itis.galeev.android.myapplication.presentation

import android.os.Bundle
import android.os.PersistableBundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import ru.kpfu.itis.galeev.android.myapplication.R
import ru.kpfu.itis.galeev.android.myapplication.presentation.base.BaseActivity
import ru.kpfu.itis.galeev.android.myapplication.databinding.ActivityMainBinding
import by.kirich1409.viewbindingdelegate.viewBinding

class MainActivity : BaseActivity(R.layout.activity_main) {
    override val fragmentContainerId = R.id.main_activity_container
    private val viewBinding by viewBinding(ActivityMainBinding::bind, )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navHost =
            supportFragmentManager.findFragmentById(R.id.main_activity_container) as NavHostFragment
        val navController = navHost.navController
        NavigationUI.setupActionBarWithNavController(this, navHost.navController)
    }
}