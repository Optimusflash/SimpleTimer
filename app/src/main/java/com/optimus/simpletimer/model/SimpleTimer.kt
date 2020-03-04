package com.optimus.simpletimer.model

import android.os.CountDownTimer
import com.optimus.simpletimer.helpers.TimeUnits
import com.optimus.simpletimer.helpers.TimeUtil.parseToHMS

/**
 * Created by Dmitriy Chebotar on 21.02.2020.
 */

class SimpleTimer(
    millisInFuture: Long, countDownInterval: Long = TimeUnits.SECOND.value,
    private val timerListener: (millis: Long) -> Unit
) : CountDownTimer(millisInFuture, countDownInterval) {

    override fun onTick(millisInFuture: Long) {
        timerListener.invoke(millisInFuture)
    }

    override fun onFinish() {
        //Empty
    }

    fun pause() {
        this.cancel()
    }

    fun reset() {
        this.cancel()
    }

}