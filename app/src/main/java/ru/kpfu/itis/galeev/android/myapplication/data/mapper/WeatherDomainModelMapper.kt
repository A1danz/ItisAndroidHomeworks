package ru.kpfu.itis.galeev.android.myapplication.data.mapper

import ru.kpfu.itis.galeev.android.myapplication.data.remote.pojo.response.WeatherResponse
import ru.kpfu.itis.galeev.android.myapplication.domain.model.WeatherAboutDomainModel
import ru.kpfu.itis.galeev.android.myapplication.domain.model.WeatherCoordDomainModel
import ru.kpfu.itis.galeev.android.myapplication.domain.model.WeatherDomainModel
import ru.kpfu.itis.galeev.android.myapplication.domain.model.WeatherMainDomainModel
import ru.kpfu.itis.galeev.android.myapplication.domain.model.WeatherWindDomainModel
import ru.kpfu.itis.galeev.android.myapplication.utils.Constants

class WeatherDomainModelMapper {
    fun mapDataToDomain(response : WeatherResponse) : WeatherDomainModel {
        return with(response) {
            WeatherDomainModel (
                coordData = WeatherCoordDomainModel(
                    coordData?.lon ?: Constants.EMPTY_COORDS,
                    coordData?.lat ?: Constants.EMPTY_COORDS
                ),
                mainData = WeatherMainDomainModel(
                    mainData?.temperature ?: 0f.toDouble(),
                    mainData?.feelsLike ?: 0f.toDouble(),
                    mainData?.pressure ?: 0,
                    mainData?.humidity ?: 0
                ),
                windData = WeatherWindDomainModel(
                    wind?.speed ?: 0f.toDouble(),
                    wind?.deg ?: 0,
                    wind?.gust ?: 0f.toDouble()
                ),
                aboutData = WeatherAboutDomainModel(
                    about?.get(0)?.weatherGroup ?: "",
                    about?.get(0)?.description ?: "",
                    about?.get(0)?.icon ?: ""
                ),
                cityName = cityName ?: ""
            )
        }
    }
}