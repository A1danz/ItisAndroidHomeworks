package ru.kpfu.itis.galeev.android.myapplication.presentation.screens.weather

import android.app.AlertDialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.galeev.android.myapplication.R
import ru.kpfu.itis.galeev.android.myapplication.data.exceptionhandler.runCatching
import ru.kpfu.itis.galeev.android.myapplication.data.exceptions.BadRequestException
import ru.kpfu.itis.galeev.android.myapplication.data.exceptions.EmptyWeatherException
import ru.kpfu.itis.galeev.android.myapplication.data.exceptions.NotFoundException
import ru.kpfu.itis.galeev.android.myapplication.data.exceptions.RequestException
import ru.kpfu.itis.galeev.android.myapplication.data.exceptions.ServerErrorException
import ru.kpfu.itis.galeev.android.myapplication.data.exceptions.TooManyRequestsException
import ru.kpfu.itis.galeev.android.myapplication.data.exceptions.UnauthorizedException
import ru.kpfu.itis.galeev.android.myapplication.di.ServiceLocator
import ru.kpfu.itis.galeev.android.myapplication.presentation.model.WeatherUiModel

class WeatherViewModel : ViewModel() {
    private val _currentWeatherFlow = MutableStateFlow<WeatherUiModel?>(null)
    val currentWeatherFlow : StateFlow<WeatherUiModel?> get() = _currentWeatherFlow

    val errorsChannel = Channel<Int>()

    fun getWeatherInfo(city : String) {
        viewModelScope.launch {
            runCatching(ServiceLocator.exceptionHandlerDelegate) {
                ServiceLocator.getWeatherUseCase.invoke(city)
            }.onSuccess {
                _currentWeatherFlow.emit(it)
            }.onFailure {
                val msgId = when(it) {
                    is BadRequestException -> R.string.bad_request
                    is EmptyWeatherException -> R.string.empty_weather_error
                    is NotFoundException -> R.string.not_found
                    is RequestException -> R.string.base_exception
                    is ServerErrorException -> R.string.server_error
                    is TooManyRequestsException -> R.string.many_requests_error
                    is UnauthorizedException -> R.string.unauthorized_error
                    else -> R.string.base_exception
                }

                errorsChannel.send(msgId)
            }
        }
    }

    override fun onCleared() {
        errorsChannel.close()
        super.onCleared()
    }
}