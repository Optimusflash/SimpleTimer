package com.optimus.simpletimer.di

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.optimus.simpletimer.helpers.TimerNotification
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Dmitriy Chebotar on 04.04.2020.
 */
@Module
class NotificationModule() {

    companion object {
        const val CHANNEL_ID = "timerServiceChanel"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Provides
    @Singleton
    fun provideNotificationChanel(): NotificationChannel {
        return NotificationChannel(
            CHANNEL_ID,
            "Timer service channel",
            NotificationManager.IMPORTANCE_LOW
        )
    }

    @Provides
    @Singleton
    fun provideNotificationManager(context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
}