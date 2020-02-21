package com.optimus.simpletimer.model

import android.os.CountDownTimer
import com.optimus.simpletimer.helpers.LiveDataManager
import com.optimus.simpletimer.helpers.TimeUnits
import com.optimus.simpletimer.helpers.TimeUtil.parseToHMS

/**
 * Created by Dmitriy Chebotar on 21.02.2020.
 */

class SimpleTimer(millisInFuture: Long, countDownInterval: Long = TimeUnits.SECOND.value) :
    CountDownTimer(millisInFuture, countDownInterval) {

    private val time = LiveDataManager.timeMLD
    private val isFinished = LiveDataManager.isFinishedMLD
    private val step = LiveDataManager.stepMLD
    private var secondsRemaining = 0

    init {
        parseToHMS(millisInFuture)
    }

    override fun onTick(millisInFuture: Long) {
        time.postValue(parseToHMS(millisInFuture))
        secondsRemaining = (millisInFuture / TimeUnits.SECOND.value).toInt()
        step.value = secondsRemaining
        if (millisInFuture < 1000) {
            isFinished.value = true
        }
    }

    override fun onFinish() {
        //Empty
    }

    fun pause() {
        this.cancel()
    }

    fun reset() {
        this.cancel()
        time.value = Triple(0, 0, 0)
        secondsRemaining = 0
    }

}