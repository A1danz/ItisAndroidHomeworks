package ru.kpfu.itis.galeev.android.myapplication.presentation.screens.weather

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.kpfu.itis.galeev.android.myapplication.R
import ru.kpfu.itis.galeev.android.myapplication.databinding.FragmentWeatherBinding
import ru.kpfu.itis.galeev.android.myapplication.di.ServiceLocator
import ru.kpfu.itis.galeev.android.myapplication.presentation.base.BaseFragment

class WeatherFragment : BaseFragment(R.layout.fragment_weather) {
    private val viewBinding : FragmentWeatherBinding by viewBinding(FragmentWeatherBinding::bind)

    private val viewModel : WeatherViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        observeData()
        with(viewBinding) {
            btnGetWeather.setOnClickListener {
                if (!inputWeather.text.isNullOrBlank()) {
                    val city = inputWeather.text.toString()
                    viewModel.getWeatherInfo(city)
                }
            }
        }
    }

    private fun observeData() {
        with(viewModel) {
            lifecycleScope.launch {
                currentWeatherFlow.collect {uiModel ->
                    uiModel?.let {
                        with(viewBinding) {
                            cvWeatherInfo.isVisible = true

                            val aboutData = uiModel.aboutData
                            val coordData = uiModel.coordData
                            val windData = uiModel.windData
                            val mainData = uiModel.mainData
                            tvDescription.text = aboutData.description
                            tvTemperature.text = getString(R.string.temperature_celsius, mainData.temperature)

                            tvFeelsLikeValue.text = getString(R.string.temperature_celsius, mainData.feelsLike)
                            tvPressureValue.text = getString(R.string.pressure_value, mainData.pressure)
                            tvHumidityValue.text = getString(R.string.humidity_value, mainData.humidity)

                            tvWindSpeedValue.text = getString(R.string.wind_speed_value, windData.speed)
                            tvWindDegValue.text = getString(R.string.wind_deg_value, windData.deg)
                            tvWindGustValue.text = getString(R.string.wind_speed_value, windData.gust)

                            tvLon.text = coordData.lon.toString()
                            tvLat.text = coordData.lat.toString()
                        }
                    }
                }
            }

            lifecycleScope.launch {
                errorsChannel.consumeEach {msgId ->
                    AlertDialog.Builder(requireContext())
                        .setTitle(getString(R.string.error_title))
                        .setMessage(msgId)
                        .show()
                }
            }
        }
    }
}
