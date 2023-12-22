package ru.kpfu.itis.galeev.android.myapplication.utils

object TimeUtil {
    fun convertIntToString(time : Int) : String {
        val minutes = time / 60
        val seconds = time % 60
        val stringSeconds = if (seconds / 10 > 1) seconds.toString() else "0$seconds"
        return "$minutes:$stringSeconds"

    }
}