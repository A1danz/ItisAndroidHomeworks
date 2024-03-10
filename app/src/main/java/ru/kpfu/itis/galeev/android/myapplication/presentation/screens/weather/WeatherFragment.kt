package ru.kpfu.itis.galeev.android.myapplication.presentation.screens.weather

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.kpfu.itis.galeev.android.myapplication.BuildConfig
import ru.kpfu.itis.galeev.android.myapplication.R
import ru.kpfu.itis.galeev.android.myapplication.databinding.FragmentWeatherBinding
import ru.kpfu.itis.galeev.android.myapplication.di.ServiceLocator
import ru.kpfu.itis.galeev.android.myapplication.presentation.MainActivity
import ru.kpfu.itis.galeev.android.myapplication.presentation.base.BaseFragment

class WeatherFragment : BaseFragment(R.layout.fragment_weather) {
    private val viewBinding : FragmentWeatherBinding by viewBinding(FragmentWeatherBinding::bind)
    private val viewModel : WeatherViewModel by viewModels()
    private var infoIsVisible = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        observeData()
        with(viewBinding) {
            btnGetWeather.setOnClickListener {
                if (!inputWeather.text.isNullOrBlank()) {
                    if (inputWeather.text.toString() == BuildConfig.DEBUG_SECRET_KEY) {
                        findNavController().navigate(R.id.action_weatherFragment_to_debugMenuFragment)
                        return@setOnClickListener
                    }
                    val city = inputWeather.text.toString()
                    progressBar.isVisible = true
                    viewModel.getWeatherInfo(city)
                }
            }
        }
    }

    private fun observeData() {
        with(viewModel) {
            lifecycleScope.launch {
                currentWeatherFlow.collect {uiModel ->
                    (requireActivity() as? MainActivity)?.hideKeyboard()
                    uiModel?.let {
                        with(viewBinding) {
                            println("TEST TAG - ${uiModel.windData.gust}")
                            if (infoIsVisible.not()) {
                                infoIsVisible = true
                                val cl : ConstraintLayout = parentLayout
                                val cs = ConstraintSet()
                                cs.clone(cl)
                                cs.connect(
                                    R.id.progress_bar, ConstraintSet.BOTTOM,
                                    R.id.cv_weather_info, ConstraintSet.BOTTOM,
                                    0
                                    )
                                cs.applyTo(cl)
                            }
                            progressBar.isVisible = false
                            cvWeatherInfo.isVisible = true

                            val aboutData = uiModel.aboutData
                            val coordData = uiModel.coordData
                            val windData = uiModel.windData
                            val mainData = uiModel.mainData

                            tvCity.text = uiModel.cityName
                            tvDescription.text = aboutData.description
                            tvTemperature.text = getString(R.string.temperature_celsius, mainData.temperature)

                            tvFeelsLikeValue.text = getString(R.string.temperature_celsius, mainData.feelsLike)
                            tvPressureValue.text = getString(R.string.pressure_value, mainData.pressure)
                            tvHumidityValue.text = getString(R.string.humidity_value, mainData.humidity)

                            tvWindSpeedValue.text = getString(R.string.wind_speed_value, windData.speed.toString())
                            tvWindDegValue.text = getString(R.string.wind_deg_value, windData.deg)
                            tvWindGustValue.text = getString(R.string.wind_speed_value, windData.gust.toString())

                            tvLon.text = coordData.lon.toString()
                            tvLat.text = coordData.lat.toString()

                            println(uiModel.aboutData.icon + "THIS IS ICON")
                            Glide.with(this@WeatherFragment)
                                .load(uiModel.aboutData.icon)
                                .into(ivWeather)
                        }
                    }
                }
            }

            lifecycleScope.launch {
                errorsChannel.consumeEach {msgId ->
                    viewBinding.progressBar.isVisible = false
                    AlertDialog.Builder(requireContext())
                        .setTitle(getString(R.string.error_title))
                        .setMessage(msgId)
                        .show()
                }
            }
        }
    }
}
