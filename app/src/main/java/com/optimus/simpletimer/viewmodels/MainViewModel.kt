package com.optimus.simpletimer.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.optimus.simpletimer.extensions.default
import com.optimus.simpletimer.extensions.set
import com.optimus.simpletimer.helpers.TimeUtil.parseHMStoString
import com.optimus.simpletimer.helpers.TimeUtil.parseMillisToString
import com.optimus.simpletimer.helpers.TimeUtil.parseToMillis
import com.optimus.simpletimer.helpers.TimerState


/**
 * Created by Dmitriy Chebotar on 16.02.2020.
 */

class MainViewModel : ViewModel() {
    private val timerState = MutableLiveData<TimerState>().default(TimerState.STOPPED)
    private val time = MutableLiveData<Triple<String,Long,Int>>().default(Triple("00:00:00",0L,0))  //Time string, milliseconds, progress

    var tick = 0
    private var tickCount = 0
    // private var timeInMillis = 0L

    fun setupTimer(hours: Int, minutes: Int, seconds: Int) {
        val timeString = parseHMStoString(hours, minutes, seconds)
        val timeInMillis = parseToMillis(hours, minutes, seconds)
        time.set(Triple(timeString,timeInMillis,360))
        tickCount = timeInMillis.toInt() / 10
        tick = 360 / tickCount
    }

    fun getTimerState(): LiveData<TimerState> {
        return timerState
    }

    fun getTime(): LiveData<Triple<String,Long,Int>> {
        return time
    }


    fun updateTime(milliseconds: Long, state: TimerState) {          //From BroadcastReceiver
        val timeString = parseMillisToString(milliseconds)
        when (state) {
            TimerState.STARTED -> {
                time.set((Triple(timeString,milliseconds, milliseconds.toInt())))
                timerState.set(TimerState.STARTED)
            }
            TimerState.PAUSED -> {
                time.set((Triple(timeString,milliseconds, milliseconds.toInt())))
                timerState.set(TimerState.PAUSED)
            }
            TimerState.STOPPED -> {
                time.set((Triple(timeString,milliseconds, milliseconds.toInt())))
                timerState.set(TimerState.STOPPED)
            }
        }
    }
}