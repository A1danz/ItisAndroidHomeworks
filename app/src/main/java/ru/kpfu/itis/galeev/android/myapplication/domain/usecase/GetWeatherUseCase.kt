package ru.kpfu.itis.galeev.android.myapplication.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.galeev.android.myapplication.domain.mapper.WeatherUiModelMapper
import ru.kpfu.itis.galeev.android.myapplication.domain.repository.WeatherRepository
import ru.kpfu.itis.galeev.android.myapplication.presentation.model.WeatherUiModel

class GetWeatherUseCase(
    private val dispatcher : CoroutineDispatcher,
    private val repository: WeatherRepository,
    private val mapper : WeatherUiModelMapper
) {
    suspend fun getWeatherByCity(city : String) : WeatherUiModel {
        return withContext(dispatcher) {
            mapper.mapDomainToUi(
                repository.getWeatherByCity(city)
            )
        }
    }
}