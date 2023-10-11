package ru.kpfu.itis.galeev.android.myapplication

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.kpfu.itis.galeev.android.myapplication.base.BaseActivity
import ru.kpfu.itis.galeev.android.myapplication.base.BaseFragment
import ru.kpfu.itis.galeev.android.myapplication.databinding.ActivityMainBinding
import ru.kpfu.itis.galeev.android.myapplication.fragments.FirstFragment
import ru.kpfu.itis.galeev.android.myapplication.utils.ActionType

class MainActivity : BaseActivity() {
    override val fragmentContainerId = R.id.main_activity_container
    override val landscapeFragmentContainerId = R.id.main_activity_container_second

    var _viewBinding : ActivityMainBinding? = null
    val viewBinding : ActivityMainBinding
        get() = _viewBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _viewBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = viewBinding.root
        setContentView(view)
        supportFragmentManager.beginTransaction()
            .replace(
                fragmentContainerId,
                FirstFragment.getInstance(resources.configuration.orientation),
                FirstFragment.FIRST_FRAGMENT_TAG
            )
            .commit()
    }


    override fun moveToScreen(
        actionType: ActionType,
        destination: BaseFragment,
        orientation : Int,
        tag: String?,
        isAddToBackStack: Boolean
    ) {
        val container : Int
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            container = landscapeFragmentContainerId
        } else {
            container = fragmentContainerId
        }
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