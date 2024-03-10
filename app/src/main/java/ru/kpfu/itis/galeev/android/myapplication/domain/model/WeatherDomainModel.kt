package ru.kpfu.itis.galeev.android.myapplication.domain.model

import ru.kpfu.itis.galeev.android.myapplication.data.remote.pojo.response.CoordData

data class WeatherDomainModel(
    val coordData : WeatherCoordDomainModel,
    val mainData : WeatherMainDomainModel,
    val windData : WeatherWindDomainModel,
    val aboutData : WeatherAboutDomainModel,
    val cityName : String
)

fun WeatherDomainModel.isTotalEmpty() : Boolean {
    val isMainDataEmpty = with(this.mainData) {
        feelsLike == 0.0 && temperature == 0.0 && pressure == 0 && humidity == 0
    }

    val isCoordEmpty = with(this.coordData) {
        lon == 0.0 && lat == 0.0
    }

    val isWindEmpty = with(this.windData) {
        speed == 0.0 && gust == 0.0 && deg == 0
    }

    val isAboutEmpty = with(this.aboutData) {
        description.isEmpty() && icon.isEmpty() && weatherGroup.isEmpty()
    }

    return isMainDataEmpty && isCoordEmpty && isWindEmpty && isAboutEmpty && cityName.isEmpty()
}
