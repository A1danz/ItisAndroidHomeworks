package ru.kpfu.itis.galeev.android.myapplication.domain.mapper

import ru.kpfu.itis.galeev.android.myapplication.domain.model.WeatherDomainModel
import ru.kpfu.itis.galeev.android.myapplication.presentation.model.WeatherAboutUiModel
import ru.kpfu.itis.galeev.android.myapplication.presentation.model.WeatherCoordUiModel
import ru.kpfu.itis.galeev.android.myapplication.presentation.model.WeatherMainUiModel
import ru.kpfu.itis.galeev.android.myapplication.presentation.model.WeatherUiModel
import ru.kpfu.itis.galeev.android.myapplication.presentation.model.WeatherWindUiModel
import ru.kpfu.itis.galeev.android.myapplication.utils.OpenWeatherIconGetter

class WeatherUiModelMapper {
    fun mapDomainToUi(weatherDomainModel: WeatherDomainModel) : WeatherUiModel {
        return with(weatherDomainModel) {
            WeatherUiModel(
                coordData = WeatherCoordUiModel(
                    this.coordData.lon,
                    this.coordData.lat
                ),
                mainData = WeatherMainUiModel(
                    this.mainData.temperature.toInt(),
                    this.mainData.feelsLike.toInt(),
                    this.mainData.pressure,
                    this.mainData.humidity
                ),
                windData = WeatherWindUiModel(
                    this.windData.speed,
                    this.windData.deg,
                    this.windData.gust
                ),
                aboutData = WeatherAboutUiModel(
                    this.aboutData.weatherGroup,
                    this.aboutData.description,
                    OpenWeatherIconGetter.getIconLink(this.aboutData.icon)
                )
            )
        }
    }
}