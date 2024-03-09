package ru.kpfu.itis.galeev.android.myapplication.data.remote.pojo.response

import com.google.gson.annotations.SerializedName

class WeatherResponse(
    @SerializedName("coord")
    val coordData : CoordData? = null,
    @SerializedName("main")
    val mainData : MainData? = null,
    val wind : Wind? = null,
    val about : Array<AboutWeather>? = null
)

class CoordData(
    val lon : Double? = null,
    val lat : Double? = null
)


class MainData (
    @SerializedName("temp")
    val temperature : Double? = null,
    @SerializedName("feels_like")
    val feelsLike : Double? = null,
    val pressure : Int? = null,
    val humidity : Int? = null
)

class Wind (
    val speed : Double? = null,
    val deg : Int? = null,
    val gust : Double? = null
)

class AboutWeather (
    @SerializedName("main")
    val weatherGroup : String? = null,
    val description : String? = null,
    val icon : String? = null
)