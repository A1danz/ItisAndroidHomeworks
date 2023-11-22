package ru.kpfu.itis.galeev.android.myapplication.fragments

import android.app.Notification
import android.app.NotificationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ru.kpfu.itis.galeev.android.myapplication.R
import ru.kpfu.itis.galeev.android.myapplication.base.BaseActivity
import ru.kpfu.itis.galeev.android.myapplication.databinding.FragmentSettingsBinding
import ru.kpfu.itis.galeev.android.myapplication.utils.NotificationConfig

class SettingsFragment : Fragment() {
    var _viewBinding : FragmentSettingsBinding? = null
    val viewBinding : FragmentSettingsBinding get() = _viewBinding!!
    var importanceSwitchGroup : MutableList<SwitchCompat> = mutableListOf()
    var privacySwitchGroup : MutableList<SwitchCompat> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentSettingsBinding.inflate(inflater)
        (requireActivity() as BaseActivity).changeTitleBar()
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewBinding) {
            val changeDefaultPrivacyMessage : String = ContextCompat.getString(requireContext(), R.string.change_default_privacy_alert)
            importanceSwitchGroup = mutableListOf(switchImportanceMedium, switchImportanceHigh, switchImportanceUrgent)
            privacySwitchGroup = mutableListOf(switchPrivacyPublic, switchPrivacyPrivate, switchPrivacySecret)
            checkAllSwitches(this)
            val switchImportanceCheckedChangeListener = object : CompoundButton.OnCheckedChangeListener {
                override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                    val switch = buttonView as? SwitchCompat
                    switch?.let { switchCompat ->
                        if (isChecked) {
                            unSwitchAll(switchCompat, importanceSwitchGroup)
                            when (switchCompat) {
                                switchImportanceMedium -> {
                                    NotificationConfig.importance = NotificationManager.IMPORTANCE_LOW
                                }
                                switchImportanceHigh -> {
                                    NotificationConfig.importance = NotificationManager.IMPORTANCE_DEFAULT
                                }
                                switchImportanceUrgent -> {
                                    NotificationConfig.importance = NotificationManager.IMPORTANCE_HIGH
                                }
                            }
                        } else {
                            NotificationConfig.importance = NotificationManager.IMPORTANCE_MIN
                        }
                    }
                }
            }

            val switchPrivacyCheckedChangeListener = object : CompoundButton.OnCheckedChangeListener {
                override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                    val switch = (buttonView as? SwitchCompat)
                    switch?.let { switchCompat ->
                        if (isChecked) {
                            unSwitchAll(switchCompat, privacySwitchGroup)
                            when (switchCompat) {
                                switchPrivacyPublic -> {
                                    NotificationConfig.privacyVisibility = Notification.VISIBILITY_PUBLIC
                                }
                                switchPrivacyPrivate -> {
                                    NotificationConfig.privacyVisibility = Notification.VISIBILITY_PRIVATE
                                }
                                switchPrivacySecret -> {
                                    NotificationConfig.privacyVisibility = Notification.VISIBILITY_SECRET
                                }
                            }
                        } else {
                            var checkedSwitches = 0
                            privacySwitchGroup.forEach { if (it.isChecked) checkedSwitches++ }
                            if (checkedSwitches == 0) {
                                switchPrivacyPrivate.isChecked = true
                                NotificationConfig.privacyVisibility = Notification.VISIBILITY_PRIVATE
                                Toast.makeText(requireContext(), changeDefaultPrivacyMessage, Toast.LENGTH_SHORT).show()
                            }
                        }
                        println("TEST TAG - ${NotificationConfig.privacyVisibility}")
                    }
                }
            }

            importanceSwitchGroup.map { switchCompat ->
                switchCompat.setOnCheckedChangeListener(switchImportanceCheckedChangeListener)
            }
            privacySwitchGroup.map { switchCompat ->
                switchCompat.setOnCheckedChangeListener(switchPrivacyCheckedChangeListener)
            }
            switchFeatureBtn.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
                override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                    NotificationConfig.withBtn = isChecked
                }
            })
            switchFeatureTextSize.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
                override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                    NotificationConfig.withLongText = isChecked
                }
            })

        }
    }

    fun checkAllSwitches(viewBinding : FragmentSettingsBinding) {
        with (viewBinding) {
            when (NotificationConfig.importance) {
                NotificationManager.IMPORTANCE_MIN -> switchImportanceMedium.isChecked = true
                NotificationManager.IMPORTANCE_DEFAULT -> switchImportanceHigh.isChecked = true
                NotificationManager.IMPORTANCE_HIGH -> switchImportanceUrgent.isChecked = true
            }
            when (NotificationConfig.privacyVisibility) {
                Notification.VISIBILITY_PUBLIC -> switchPrivacyPublic.isChecked = true
                Notification.VISIBILITY_PRIVATE -> switchPrivacyPrivate.isChecked = true
                Notification.VISIBILITY_SECRET -> switchPrivacySecret.isChecked = true
            }

            switchFeatureTextSize.isChecked = NotificationConfig.withLongText
            switchFeatureBtn.isChecked = NotificationConfig.withBtn
        }
    }

    fun unSwitchAll(checkedSwitch : SwitchCompat, switchGroup : MutableList<SwitchCompat>) {
        switchGroup.map { switchCompat -> switchCompat.isChecked = (switchCompat == checkedSwitch)  }
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
        importanceSwitchGroup.clear()
        privacySwitchGroup.clear()
    }
}