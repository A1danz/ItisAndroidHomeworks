package ru.kpfu.itis.galeev.android.myapplication.base

import androidx.appcompat.app.AppCompatActivity
import ru.kpfu.itis.galeev.android.myapplication.utils.ActionType

abstract class BaseActivity : AppCompatActivity() {
    protected abstract val fragmentContainerId : Int
    protected abstract val landscapeFragmentContainerId : Int

    abstract fun moveToScreen(
        actionType : ActionType,
        destination : BaseFragment,
        orientation : Int, // Configuration.ORIENTATION_PORTRAIT || Configuration.ORIENTATION_LANDSCAPE
        tag : String? = null,
        isAddToBackStack : Boolean = true
    )

}