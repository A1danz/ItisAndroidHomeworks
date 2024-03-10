package ru.kpfu.itis.galeev.android.myapplication.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import ru.kpfu.itis.galeev.android.myapplication.data.remote.pojo.response.WeatherResponse

interface OpenWeatherApi {
    @GET("weather")
    suspend fun getWeatherByCity(@Query(value = "q") city : String) : WeatherResponse
}