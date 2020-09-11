package com.optimus.simpletimer.repositories

import android.util.Log
import com.optimus.simpletimer.database.TimerDao
import com.optimus.simpletimer.di.App
import com.optimus.simpletimer.model.TimerData
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Dmitriy Chebotar on 20.03.2020.
 */

class MainRepository @Inject constructor(private val timerDao: TimerDao) {

    fun updateData(timerData: TimerData): Completable {
        Log.e("M_MainRepository", "triggered")
        return timerDao.insert(timerData)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getData(): Single<TimerData> {
        return timerDao.get()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
    }
}