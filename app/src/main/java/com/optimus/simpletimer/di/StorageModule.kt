package com.optimus.simpletimer.di

import android.content.Context
import androidx.room.Room
import com.optimus.simpletimer.database.TimerDao
import com.optimus.simpletimer.database.TimerDataBase
import com.optimus.simpletimer.repositories.MainRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Dmitriy Chebotar on 04.04.2020.
 */

@Module
class StorageModule(private val appContext: Context) {

    @Provides
    @Singleton
    fun provideAppContext() = appContext

    @Provides
    @Singleton
    fun provideRoomDatabase(): TimerDataBase{
        return Room.databaseBuilder(appContext, TimerDataBase::class.java, "timer_database").build()
    }
    @Provides
    @Singleton
    fun provideRoomDao(timerDataBase : TimerDataBase): TimerDao{
        return timerDataBase.timerDao()
    }

}