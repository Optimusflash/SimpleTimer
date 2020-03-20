package com.optimus.simpletimer.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.optimus.simpletimer.model.TimerData

/**
 * Created by Dmitriy Chebotar on 20.03.2020.
 */

@Database(entities = [TimerData::class], version = 1, exportSchema = false)
abstract class TimerDataBase: RoomDatabase() {
    abstract fun timerDao(): TimerDao
}