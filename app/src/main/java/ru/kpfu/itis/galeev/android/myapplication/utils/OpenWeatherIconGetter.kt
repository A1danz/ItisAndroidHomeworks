package ru.kpfu.itis.galeev.android.myapplication.utils

import ru.kpfu.itis.galeev.android.myapplication.BuildConfig

object OpenWeatherIconGetter {
    fun getIconLink(icon : String) : String {
        return "${BuildConfig.OPEN_WEATHER_ICON_URL}img/wn/$icon@2x.png"
    }
}