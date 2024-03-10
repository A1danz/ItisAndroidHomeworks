package ru.kpfu.itis.galeev.android.myapplication.presentation.screens.debugmenu

import android.os.Build
import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kpfu.itis.galeev.android.myapplication.BuildConfig
import ru.kpfu.itis.galeev.android.myapplication.R
import ru.kpfu.itis.galeev.android.myapplication.databinding.FragmentDebugMenuBinding
import ru.kpfu.itis.galeev.android.myapplication.presentation.base.BaseFragment

class DebugMenuFragment : BaseFragment(R.layout.fragment_debug_menu) {
    private val viewBinding : FragmentDebugMenuBinding by viewBinding(FragmentDebugMenuBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewBinding) {
            tvAppNameValue.text = getString(R.string.app_name)
            tvBaseUrlValue.text = BuildConfig.OPEN_WEATHER_BASE_URL.replace("https://", "")
            tvVersionCodeNameValue.text = getString(
                R.string.version_name_version_code,
                BuildConfig.VERSION_NAME,
                BuildConfig.VERSION_CODE
            )
            tvDeviceInfoValue.text = getString(
                R.string.device_info,
                Build.MANUFACTURER,
                Build.MODEL
                )
            tvAndroidVersionValue.text = getString(
                R.string.android_version,
                "Android ${Build.VERSION.RELEASE}" ,
                Build.VERSION.SDK_INT
            )
        }
    }
}