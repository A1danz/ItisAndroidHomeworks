package ru.kpfu.itis.galeev.android.myapplication.base

import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import ru.kpfu.itis.galeev.android.myapplication.utils.ActionType

abstract class BaseActivity : AppCompatActivity() {
    protected abstract val fragmentContainerId : Int

    abstract fun moveToScreen(
        actionType : ActionType,
        destination : Fragment,
        tag : String? = null,
        isAddToBackStack : Boolean = true
    )

    abstract fun sharedTransition(cv : CardView, position : Int)
}