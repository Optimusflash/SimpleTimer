package com.optimus.simpletimer.di

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Room
import com.facebook.stetho.Stetho
import com.optimus.simpletimer.database.TimerDataBase
import javax.inject.Inject

/**
 * Created by Dmitriy Chebotar on 21.02.2020.
 */
class App : Application() {

    companion object {
        lateinit var component: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        initDagger()
    }


    private fun initDagger() {
        component = DaggerAppComponent.builder()
            .notificationModule(NotificationModule())
            .storageModule(StorageModule(this))
            .build()
    }
}