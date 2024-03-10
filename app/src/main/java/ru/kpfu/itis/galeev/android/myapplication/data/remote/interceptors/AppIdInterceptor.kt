package ru.kpfu.itis.galeev.android.myapplication.data.remote.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import ru.kpfu.itis.galeev.android.myapplication.BuildConfig
import ru.kpfu.itis.galeev.android.myapplication.utils.Keys

class AppIdInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newUrl = chain.request().url.newBuilder()
            .addQueryParameter(Keys.APP_ID_KEY, BuildConfig.APP_ID_KEY)
            .addQueryParameter(Keys.LANG.first, Keys.LANG.second)
            .addQueryParameter(Keys.UNITS.first, Keys.UNITS.second)
            .build()

        val requestBuilder = chain.request().newBuilder().url(newUrl)

        return chain.proceed(requestBuilder.build())
    }
}