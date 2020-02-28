package com.optimus.simpletimer.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.optimus.simpletimer.extensions.default
import com.optimus.simpletimer.extensions.set
import com.optimus.simpletimer.helpers.LiveDataManager
import com.optimus.simpletimer.helpers.TimeUnits
import com.optimus.simpletimer.helpers.TimeUtil.parseToMillis
import com.optimus.simpletimer.helpers.TimeUtil.parseToHMS
import com.optimus.simpletimer.helpers.TimerState
import com.optimus.simpletimer.model.SimpleTimer
import java.util.*


/**
 * Created by Dmitriy Chebotar on 16.02.2020.
 */

class MainViewModel : ViewModel() {

    private val time = LiveDataManager.timeMLD
    private val isFinished = LiveDataManager.isFinishedMLD
    private val step = LiveDataManager.stepMLD

    private val timerState = MutableLiveData<TimerState>().default(TimerState.STOPPED)

    private var timeInMillis = 0L
    private var timeInSeconds = 0
    private var timer: SimpleTimer? = null

    fun setupTimer(hours: Int, minutes: Int, seconds: Int) {
        timeInMillis = parseToMillis(hours, minutes, seconds)
        time.value = parseToHMS(timeInMillis)
        timeInSeconds = (timeInMillis / TimeUnits.SECOND.value).toInt()
    }

    fun getTimerState(): LiveData<TimerState>{
        return timerState
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
        timeInMillis = parseToMillis(time.value)
        timer = SimpleTimer(timeInMillis)
        timer?.start()
        timerState.set(TimerState.STARTED)
    }

    fun pauseTimer() {
        timeInMillis = parseToMillis(time.value)
        timer?.pause()
        timerState.set(TimerState.PAUSED)
    }

    fun resetTimer() {
        timer?.reset()
        timerState.set(TimerState.STOPPED)
    }
}