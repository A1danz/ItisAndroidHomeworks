package ru.kpfu.itis.galeev.android.myapplication.data.exceptionhandler

import retrofit2.HttpException
import ru.kpfu.itis.galeev.android.myapplication.data.exceptions.BadRequestException
import ru.kpfu.itis.galeev.android.myapplication.data.exceptions.NotFoundException
import ru.kpfu.itis.galeev.android.myapplication.data.exceptions.RequestException
import ru.kpfu.itis.galeev.android.myapplication.data.exceptions.ServerErrorException
import ru.kpfu.itis.galeev.android.myapplication.data.exceptions.TooManyRequestsException
import ru.kpfu.itis.galeev.android.myapplication.data.exceptions.UnauthorizedException


class ExceptionHandlerDelegate {
    fun handleException(ex : Throwable) : Throwable {
        if (ex !is HttpException) return ex
        if (ex.code() in 500..599) return ServerErrorException()
        return when(ex.code()) {
            400 -> BadRequestException()
            401 -> UnauthorizedException()
            404 -> NotFoundException()
            429 -> TooManyRequestsException()
            else -> RequestException()
        }
    }
}