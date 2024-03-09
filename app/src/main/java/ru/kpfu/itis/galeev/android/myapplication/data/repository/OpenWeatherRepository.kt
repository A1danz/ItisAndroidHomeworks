package ru.kpfu.itis.galeev.android.myapplication.data.repository

import ru.kpfu.itis.galeev.android.myapplication.data.exceptions.EmptyWeatherException
import ru.kpfu.itis.galeev.android.myapplication.data.mapper.WeatherDomainModelMapper
import ru.kpfu.itis.galeev.android.myapplication.data.remote.OpenWeatherApi
import ru.kpfu.itis.galeev.android.myapplication.domain.model.WeatherDomainModel
import ru.kpfu.itis.galeev.android.myapplication.domain.model.isTotalEmpty
import ru.kpfu.itis.galeev.android.myapplication.domain.repository.WeatherRepository

class OpenWeatherRepository(
    private val api : OpenWeatherApi,
    private val domainModelMapper : WeatherDomainModelMapper
) : WeatherRepository {
    override suspend fun getWeatherByCity(city : String): WeatherDomainModel {
        val domainModel = domainModelMapper.mapDataToDomain(
            api.getWeatherByCity(city)
        )

        return if (domainModel != null && !domainModel.isTotalEmpty()) {
            domainModel
        } else {
            throw EmptyWeatherException()
        }

    }
}