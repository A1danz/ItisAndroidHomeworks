package ru.kpfu.itis.galeev.android.myapplication.presentation.screens.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.kpfu.itis.galeev.android.myapplication.R
import ru.kpfu.itis.galeev.android.myapplication.databinding.FragmentWeatherBinding
import ru.kpfu.itis.galeev.android.myapplication.di.ServiceLocator
import ru.kpfu.itis.galeev.android.myapplication.presentation.base.BaseFragment

class WeatherFragment : BaseFragment(R.layout.fragment_weather) {
    private val viewBinding : FragmentWeatherBinding by viewBinding(FragmentWeatherBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        with(viewBinding) {
            btnGetWeather.setOnClickListener {
                if (!inputWeather.text.isNullOrBlank()) {
                    val city = inputWeather.text.toString()
                    lifecycleScope.launch {
                        progressBar.isVisible = true
                        val weather = ServiceLocator.getWeatherUseCase.getWeatherByCity(city)
                        println("THIS IS WEATHER - $weather")
                        progressBar.isVisible = false
                    }
                }
            }
        }
    }
}
