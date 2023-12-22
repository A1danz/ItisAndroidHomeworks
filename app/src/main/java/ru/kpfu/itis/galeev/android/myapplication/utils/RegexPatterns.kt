package ru.kpfu.itis.galeev.android.myapplication.utils

object RegexPatterns {
    val EMAIL_REGEX: Regex = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}".toRegex()
    val PHONE_REGEX : Regex = "^\\+7 \\(\\d{3}\\)-\\d{3}-\\d{2}-\\d{2}$".toRegex()
    val NAME_REGEX : Regex = "^[a-zA-Zа-яА-Я]{3,20}$".toRegex()
    val DURATION_REGEX = Regex("^([0-5]\\d):([0-5]\\d)$")
    // 79821321122
}