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
    private val time = MutableLiveData<Pair<String,Long>>().default(Pair("00:00:00",0L))  //Time string, milliseconds, progress


    fun setupTimer(hours: Int, minutes: Int, seconds: Int) {
        val timeString = parseHMStoString(hours, minutes, seconds)
        val timeInMillis = parseToMillis(hours, minutes, seconds)
        time.set(Pair(timeString,timeInMillis))
    }

    fun getTimerState(): LiveData<TimerState> {
        return timerState
    }

    fun getTime(): LiveData<Pair<String,Long>> {
        return time
    }

    fun updateTime(milliseconds: Long, state: TimerState) {          //From BroadcastReceiver
        val timeString = parseMillisToString(milliseconds)
        when (state) {
            TimerState.STARTED -> {
                time.set((Pair(timeString,milliseconds)))
                timerState.set(TimerState.STARTED)
            }
            TimerState.PAUSED -> {
                time.set((Pair(timeString,milliseconds)))
                timerState.set(TimerState.PAUSED)
            }
            TimerState.STOPPED -> {
                time.set((Pair(timeString,milliseconds)))
                timerState.set(TimerState.STOPPED)
            }
        }
    }
}