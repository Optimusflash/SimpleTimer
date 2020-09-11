package com.optimus.simpletimer.di

import com.optimus.simpletimer.activities.MainActivity
import com.optimus.simpletimer.services.TimerService
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Dmitriy Chebotar on 04.04.2020.
 */
@Singleton
@Component(modules = [NotificationModule::class, StorageModule::class, ViewModelModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(timerService: TimerService)
}