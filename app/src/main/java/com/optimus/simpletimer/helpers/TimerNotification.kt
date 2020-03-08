package com.optimus.simpletimer.helpers

import android.app.IntentService
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import com.optimus.simpletimer.App
import com.optimus.simpletimer.R
import com.optimus.simpletimer.activities.MainActivity

/**
 * Created by Dmitriy Chebotar on 08.03.2020.
 */
object TimerNotification {
    private const val NOTIFICATION_ID = 121212

    fun showNotification(context: Context) {
        val actionIntent = Intent(context, MainActivity::class.java)
        val actionPendingIntent = PendingIntent.getActivity(
            context,
            0,
            actionIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notification = NotificationCompat.Builder(context, App.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_timer_black_24dp)
            .setContentTitle("Simple timer")
            .setContentText("Running...")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setAutoCancel(true)
            .setContentIntent(actionPendingIntent)
            .build()

        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    fun hideNotification(context: Context){
        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(NOTIFICATION_ID)
    }
}