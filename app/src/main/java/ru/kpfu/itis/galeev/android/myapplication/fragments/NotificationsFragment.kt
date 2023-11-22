package ru.kpfu.itis.galeev.android.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.kpfu.itis.galeev.android.myapplication.MainActivity
import ru.kpfu.itis.galeev.android.myapplication.base.BaseActivity
import ru.kpfu.itis.galeev.android.myapplication.databinding.FragmentNotificationsBinding
import ru.kpfu.itis.galeev.android.myapplication.utils.NotificationConfig
import ru.kpfu.itis.galeev.android.myapplication.utils.NotificationHandler

class NotificationsFragment : Fragment() {
    var _viewBinding : FragmentNotificationsBinding? = null
    val viewBinding : FragmentNotificationsBinding get() = _viewBinding!!
    val notificationHandler = NotificationHandler()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentNotificationsBinding.inflate(inflater)
        (requireActivity() as BaseActivity).changeTitleBar()
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    fun initViews() {
        with(viewBinding) {
            btnCreateNotif.setOnClickListener {
                (requireActivity() as? MainActivity)?.requestPermission()
                notificationHandler.createNotification(
                    requireContext(),
                    NotificationConfig.notificationId++,
                    MAIN_NOTFICATION_CHANNEL_ID,
                    etTitleInput.text.toString(),
                    etTextInput.text.toString()
                    )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }

    companion object {
        val MAIN_NOTFICATION_CHANNEL_ID = "A1danz_app_channel"
    }
}