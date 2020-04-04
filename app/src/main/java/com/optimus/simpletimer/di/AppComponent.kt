package com.optimus.simpletimer.di

import android.app.Application
import com.optimus.simpletimer.helpers.TimerNotification
import com.optimus.simpletimer.repositories.MainRepository
import com.optimus.simpletimer.services.TimerService
import com.optimus.simpletimer.viewmodels.MainViewModel
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Dmitriy Chebotar on 04.04.2020.
 */
@Singleton
@Component(modules = [NotificationModule::class, StorageModule::class])
interface AppComponent {
    fun inject(application: Application)
    fun inject(mainViewModel: MainViewModel)
    fun inject(mainRepository: MainRepository)
    fun inject(timerNotification: TimerNotification)
    fun inject(timerService: TimerService)

}