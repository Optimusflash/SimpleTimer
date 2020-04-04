package com.optimus.simpletimer.helpers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.optimus.simpletimer.R
import com.optimus.simpletimer.activities.MainActivity
import com.optimus.simpletimer.di.App
import com.optimus.simpletimer.di.NotificationModule.Companion.CHANNEL_ID
import javax.inject.Inject

/**
 * Created by Dmitriy Chebotar on 08.03.2020.
 */

class TimerNotification {
    @Inject
    lateinit var notificationChannel: NotificationChannel

    @Inject
    lateinit var notificationManager: NotificationManager

    companion object {
        const val NOTIFICATION_ID = 121212
    }

    init {
        App.component.inject(this)
        createNotificationChanel()
    }

    private fun createNotificationChanel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    fun getNotification(context: Context): Notification {
        val actionIntent = Intent(context, MainActivity::class.java)
        val actionPendingIntent = PendingIntent.getActivity(
            context,
            0,
            actionIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_timer_black_24dp)
            .setContentTitle("Simple timer")
            .setContentText("Running...")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setAutoCancel(true)
            .setContentIntent(actionPendingIntent)
            .build()
    }
}