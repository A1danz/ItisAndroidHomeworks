package ru.kpfu.itis.galeev.android.myapplication

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import ru.kpfu.itis.galeev.android.myapplication.base.BaseActivity
import ru.kpfu.itis.galeev.android.myapplication.base.BaseFragment
import ru.kpfu.itis.galeev.android.myapplication.databinding.ActivityMainBinding
import ru.kpfu.itis.galeev.android.myapplication.fragments.StartFragment
import ru.kpfu.itis.galeev.android.myapplication.utils.ActionType

class MainActivity : BaseActivity() {
    override val fragmentContainerId = R.id.main_activity_container

    var _viewBinding : ActivityMainBinding? = null
    val viewBinding : ActivityMainBinding
        get() = _viewBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _viewBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = viewBinding.root
        setContentView(view)

        moveToScreen(
            ActionType.REPLACE,
            StartFragment(),
            StartFragment.START_FRAGMENT_TAG,
            false
        )
    }


    override fun moveToScreen(
        actionType: ActionType,
        destination: Fragment,
        tag: String?,
        isAddToBackStack: Boolean
    ) {
        val container : Int = fragmentContainerId;
        supportFragmentManager.beginTransaction().apply {
            when (actionType) {
                ActionType.ADD -> {
                    this.add(container, destination, tag)
                }

                ActionType.REPLACE -> {
                    this.replace(container, destination, tag)
                }

                ActionType.REMOVE -> {
                    this.remove(destination)
                }

                else -> Unit
            }

            if (isAddToBackStack) {
                this.addToBackStack(null)
            }
        }.commit()
    }

    override fun onDestroy() {
        _viewBinding = null
        println("onDestroyCalled")
        super.onDestroy()
    }
}