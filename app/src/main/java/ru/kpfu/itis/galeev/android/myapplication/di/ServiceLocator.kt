package ru.kpfu.itis.galeev.android.myapplication.di

import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.kpfu.itis.galeev.android.myapplication.BuildConfig
import ru.kpfu.itis.galeev.android.myapplication.data.exceptionhandler.ExceptionHandlerDelegate
import ru.kpfu.itis.galeev.android.myapplication.data.mapper.WeatherDomainModelMapper
import ru.kpfu.itis.galeev.android.myapplication.data.remote.OpenWeatherApi
import ru.kpfu.itis.galeev.android.myapplication.data.remote.interceptors.AppIdInterceptor
import ru.kpfu.itis.galeev.android.myapplication.data.repository.OpenWeatherRepository
import ru.kpfu.itis.galeev.android.myapplication.domain.mapper.WeatherUiModelMapper
import ru.kpfu.itis.galeev.android.myapplication.domain.repository.WeatherRepository
import ru.kpfu.itis.galeev.android.myapplication.domain.usecase.GetWeatherUseCase
import java.lang.Exception

object ServiceLocator {
    private lateinit var okHttpClient : OkHttpClient
    private lateinit var openWeatherApi : OpenWeatherApi
    private lateinit var weatherRepo : WeatherRepository
    lateinit var getWeatherUseCase: GetWeatherUseCase
        private set

    val exceptionHandlerDelegate = ExceptionHandlerDelegate()
    private val weatherDomainModelMapper = WeatherDomainModelMapper()
    private val weatherUiModelMapper = WeatherUiModelMapper()


    fun initDataDependencies() {
        okHttpClient = createHttpClient()
        val builder = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.OPEN_WEATHER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        openWeatherApi = builder.create(OpenWeatherApi::class.java)

    }

    fun initDomainDependencies() {
        weatherRepo = OpenWeatherRepository(
            openWeatherApi,
            weatherDomainModelMapper
        )

        getWeatherUseCase = GetWeatherUseCase(
            Dispatchers.IO,
            weatherRepo,
            weatherUiModelMapper
        )
    }

    fun getHttpClient() : OkHttpClient {
        return okHttpClient
    }
    private fun createHttpClient() : OkHttpClient {
        val httpClientBuilder = OkHttpClient.Builder().addInterceptor(AppIdInterceptor())


        return httpClientBuilder.build()
    }


}