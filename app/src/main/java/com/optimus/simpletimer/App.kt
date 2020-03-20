package com.optimus.simpletimer

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.room.Room
import com.optimus.simpletimer.database.TimerDataBase

/**
 * Created by Dmitriy Chebotar on 21.02.2020.
 */
class App: Application() {
    companion object{
        const val CHANNEL_ID = "timerServiceChanel"

        private var dataBase: TimerDataBase? = null

        fun getDatabase(): TimerDataBase{
            return dataBase!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChanel()
        createDatabase()
    }

    private fun createDatabase() {
        dataBase = Room.databaseBuilder(this, TimerDataBase::class.java, "timer_database").build()
    }

    private fun createNotificationChanel() {
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Timer service channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(serviceChannel)
        }
    }
}