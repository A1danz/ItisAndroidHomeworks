package ru.kpfu.itis.galeev.android.myapplication.utils

import android.app.Notification
import android.app.NotificationManager

object NotificationConfig {
    var notificationId = 1
    var importance : Int = NotificationManager.IMPORTANCE_MIN
    val importance_default_name = "default_importance"
    val importance_medium_name = "medium_importance"
    val importance_high_name = "high_importance"
    val importance_urgent_name = "urgent_importance"

    var privacyVisibility : Int = Notification.VISIBILITY_PRIVATE
    var withLongText = false
    var withBtn = false

    fun getPriorityByImportance() : Int {
        return when (importance) {
            NotificationManager.IMPORTANCE_MIN -> Notification.PRIORITY_MIN
            NotificationManager.IMPORTANCE_LOW -> Notification.PRIORITY_LOW
            NotificationManager.IMPORTANCE_DEFAULT -> Notification.PRIORITY_DEFAULT
            NotificationManager.IMPORTANCE_HIGH -> Notification.PRIORITY_HIGH
            else -> throw RuntimeException("importance not found")
        }
    }

    fun getNameByImportance(importance : Int) : String {
        return when (importance) {
            NotificationManager.IMPORTANCE_MIN -> importance_default_name
            NotificationManager.IMPORTANCE_LOW -> importance_medium_name
            NotificationManager.IMPORTANCE_DEFAULT -> importance_high_name
            NotificationManager.IMPORTANCE_HIGH -> importance_urgent_name
            else -> throw RuntimeException("importance not found")
        }
    }
}