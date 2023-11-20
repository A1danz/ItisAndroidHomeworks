package ru.kpfu.itis.galeev.android.myapplication.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import ru.kpfu.itis.galeev.android.myapplication.R

class NotificationHandler() {
    private var mainChannelName : String = ""


    fun createNotification(ctx : Context, id : Int, channelId : String, title : String, text : String) {
        if (mainChannelName.isEmpty()) {
            mainChannelName = ContextCompat.getString(ctx, R.string.aidans_channel_name)
        }

        ((ctx.getSystemService(Context.NOTIFICATION_SERVICE)) as? NotificationManager)?.let {manager ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    mainChannelName,
                    NotificationManager.IMPORTANCE_DEFAULT)
                manager.createNotificationChannel(channel)
            }

            val notification = NotificationCompat.Builder(ctx, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(text)

            manager.notify(id, notification.build())
        }
    }
}