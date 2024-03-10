package ru.kpfu.itis.galeev.android.myapplication.presentation

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.navigation.NavHost
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kpfu.itis.galeev.android.myapplication.R
import ru.kpfu.itis.galeev.android.myapplication.databinding.ActivityMainBinding
import ru.kpfu.itis.galeev.android.myapplication.presentation.base.BaseActivity


class MainActivity : BaseActivity(R.layout.activity_main) {
    override val fragmentContainerId = R.id.main_activity_container
    private val viewBinding by viewBinding(ActivityMainBinding::bind)
    private var navHost: NavHostFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navHost =
            supportFragmentManager.findFragmentById(R.id.main_activity_container) as NavHostFragment
        navHost?.let {
         NavigationUI.setupActionBarWithNavController(this, it.navController)
        }
    }

    fun hideKeyboard() {
        val activity = this
        val imm = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp() || navHost?.navController?.navigateUp() ?: false
    }

    override fun onDestroy() {
        super.onDestroy()
        navHost = null
    }
}