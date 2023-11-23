package ru.kpfu.itis.galeev.android.myapplication.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import ru.kpfu.itis.galeev.android.myapplication.MainActivity
import ru.kpfu.itis.galeev.android.myapplication.R

class NotificationHandler {
    private var mainChannelName : String = ""
    private var manager : NotificationManager? = null
    private var channels : HashMap<Int, NotificationChannel> = hashMapOf()
    private var actionOpenIntent : PendingIntent? = null
    private var actionSettingsIntent : PendingIntent? = null
    private var actionTapIntent : PendingIntent? = null
    private val pendingIntentFlags : Int = PendingIntent.FLAG_MUTABLE

    fun createNotification(ctx : Context, id : Int, channelId : String, title : String, text : String) {
        if (mainChannelName.isEmpty()) {
            mainChannelName = ContextCompat.getString(ctx, R.string.aidans_channel_name)
        }
        if (manager == null) {
            manager = ((ctx.getSystemService(Context.NOTIFICATION_SERVICE)) as? NotificationManager)
        }
        if (channels.isEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // creating channels
                listOf(
                    NotificationManager.IMPORTANCE_MIN, NotificationManager.IMPORTANCE_LOW,
                    NotificationManager.IMPORTANCE_DEFAULT, NotificationManager.IMPORTANCE_HIGH
                ).forEach { channelImportance ->
                    channels.put(channelImportance, NotificationChannel(
                        channelId + NotificationConfig.getNameByImportance(channelImportance),
                        mainChannelName + NotificationConfig.getNameByImportance(channelImportance),
                        channelImportance
                    ))
                }
            }
        }
        if (actionOpenIntent == null) {
            val openAction = "OPEN_ACTION"
            val openIntent = Intent(ctx, MainActivity::class.java).apply {
                action = openAction
                putExtra(ParamsKey.WITH_TOAST, true)

            }
            val openPendingIntent: PendingIntent = PendingIntent.getActivity(
                ctx, 0, openIntent,
                pendingIntentFlags
            )
            actionOpenIntent = openPendingIntent
        }
        if (actionSettingsIntent == null) {
            val settingsAction = "OPEN_SETTINGS_ACTION"
            val settingsIntent = Intent(ctx, MainActivity::class.java).apply {
                action = settingsAction
                putExtra(ParamsKey.OPEN_SETTINGS, true)
            }
            val settingsPendingIntent : PendingIntent = PendingIntent.getActivity(
                ctx, 0, settingsIntent,
                pendingIntentFlags
            )
            actionSettingsIntent = settingsPendingIntent
        }
        if (actionTapIntent == null) {
            val openAction = "OPEN_WITHOUT_ACTION"
            val openIntent = Intent(ctx, MainActivity::class.java).apply {
                action = openAction
            }
            actionTapIntent = PendingIntent.getActivity(
                ctx, 0, openIntent,
                pendingIntentFlags
            )
        }

        manager?.let {manager ->
            var newChannelId = ""
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                manager.createNotificationChannels(channels.values.toList())
                newChannelId = channels.get(NotificationConfig.importance)?.id.toString()
            }
            val notification = NotificationCompat.Builder(ctx, newChannelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(text)
                .setVisibility(NotificationConfig.privacyVisibility)
                .setPriority(NotificationConfig.getPriorityByImportance()) // ignored in versions above
                .setContentIntent(actionTapIntent)
                .setAutoCancel(true)

            if (NotificationConfig.withLongText) {
                notification.setStyle(NotificationCompat.BigTextStyle()
                    .bigText(text))
            }

            if (NotificationConfig.withBtn) {
                notification.addAction(
                    R.drawable.ic_notfication,
                    ContextCompat.getString(ctx, R.string.open_action_btn),
                    actionOpenIntent
                )
                notification.addAction(
                    R.drawable.ic_settings,
                    ContextCompat.getString(ctx, R.string.settings_action_btn),
                    actionSettingsIntent
                )
            }
            manager.notify(id, notification.build())
        }
    }
}