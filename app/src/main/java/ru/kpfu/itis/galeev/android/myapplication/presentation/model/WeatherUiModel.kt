package ru.kpfu.itis.galeev.android.myapplication.presentation.model

import ru.kpfu.itis.galeev.android.myapplication.data.remote.pojo.response.CoordData
import ru.kpfu.itis.galeev.android.myapplication.data.remote.pojo.response.MainData

data class WeatherUiModel(
    val coordData : WeatherCoordUiModel,
    val mainData: WeatherMainUiModel,
    val windData : WeatherWindUiModel,
    val aboutData : WeatherAboutUiModel,
    val cityName : String
)

data class WeatherCoordUiModel(
    val lon : Double,
    val lat : Double
)
data class WeatherAboutUiModel (
    val weatherGroup : String,
    val description : String,
    val icon : String
)

class WeatherWindUiModel (
    val speed : Double,
    val deg : Int,
    val gust : Double
)

class WeatherMainUiModel (
    val temperature : Int,
    val feelsLike : Int,
    val pressure : Int,
    val humidity : Int
    )




