package com.optimus.simpletimer.database

import androidx.room.*
import com.optimus.simpletimer.model.TimerData
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by Dmitriy Chebotar on 20.03.2020.
 */

@Dao
interface TimerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(timerData: TimerData): Completable

    @Query("SELECT * FROM timer" )
    fun get(): Single<TimerData>
}