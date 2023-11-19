package ru.kpfu.itis.galeev.android.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.kpfu.itis.galeev.android.myapplication.base.BaseActivity
import ru.kpfu.itis.galeev.android.myapplication.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {
    var _viewBinding : FragmentNotificationsBinding? = null
    val viewBinding : FragmentNotificationsBinding get() = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentNotificationsBinding.inflate(inflater)
        (requireActivity() as BaseActivity).changeTitleBar()
        return viewBinding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }
}