package ru.kpfu.itis.galeev.android.myapplication.fragments

import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.annotation.LayoutRes
import ru.kpfu.itis.galeev.android.myapplication.base.BaseActivity
import ru.kpfu.itis.galeev.android.myapplication.base.BaseFragment

abstract class AirplaneBtnsFragment(@LayoutRes layout : Int) : BaseFragment(layout) {
    abstract fun changeBtnByModeState(state : Boolean)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeBtnByModeState(Settings.System.getInt(requireActivity().contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) == 0)
    }
}