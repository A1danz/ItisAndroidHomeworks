package ru.kpfu.itis.galeev.android.myapplication.domain.repository

import ru.kpfu.itis.galeev.android.myapplication.domain.model.WeatherDomainModel

interface WeatherRepository {
    suspend fun getWeatherByCity(city : String) : WeatherDomainModel
}