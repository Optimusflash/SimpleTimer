package com.optimus.simpletimer.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.optimus.simpletimer.helpers.LiveDataManager
import com.optimus.simpletimer.helpers.TimeUnits
import com.optimus.simpletimer.helpers.TimeUtil.parseInMillis
import com.optimus.simpletimer.helpers.TimeUtil.parseToHMS
import com.optimus.simpletimer.model.SimpleTimer


/**
 * Created by Dmitriy Chebotar on 16.02.2020.
 */

class MainViewModel : ViewModel() {

    private val time = LiveDataManager.timeMLD
    private val isFinished = LiveDataManager.isFinishedMLD
    private val step = LiveDataManager.stepMLD
    private var timeInMillis = 0L
    private var timeInSeconds = 0
    private var timer: SimpleTimer? = null

    fun setupTimer(hours: Int, minutes: Int, seconds: Int) {
        timeInMillis = parseInMillis(hours, minutes, seconds)
        time.value = parseToHMS(timeInMillis)
        timeInSeconds = (timeInMillis / TimeUnits.SECOND.value).toInt()
    }

    fun getTime(): LiveData<Triple<Int, Int, Int>> {
        return time
    }

    fun getIsFinished(): LiveData<Boolean> {
        return isFinished
    }

    fun getStep(): LiveData<Int> {
        return step
    }

    fun getTimeInSeconds(): Int {
        return timeInSeconds
    }

    fun getTimeInMillis(): Long {
        return timeInMillis
    }


    fun startTimer() {
        timeInMillis = parseInMillis(time.value)
        timer = SimpleTimer(timeInMillis)
        timer?.start()
    }

    fun pauseTimer() {
        timeInMillis = parseInMillis(time.value)
        timer?.pause()

    }

    fun resetTimer() {
        timer?.reset()
    }
}