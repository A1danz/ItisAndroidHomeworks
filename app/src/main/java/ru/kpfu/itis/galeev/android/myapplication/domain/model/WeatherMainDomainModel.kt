package ru.kpfu.itis.galeev.android.myapplication.domain.model

data class WeatherMainDomainModel(
    val temperature : Double,
    val feelsLike : Double,
    val pressure : Int,
    val humidity : Int
)