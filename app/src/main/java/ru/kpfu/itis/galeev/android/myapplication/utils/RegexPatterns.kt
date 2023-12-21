package ru.kpfu.itis.galeev.android.myapplication.utils

object RegexPatterns {
    val EMAIL_REGEX: Regex = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}".toRegex()
    val PHONE_REGEX : Regex = "".toRegex()
    val NAME_REGEX : Regex = "^[a-zA-Zа-яА-Я]{3,20}$".toRegex()
}