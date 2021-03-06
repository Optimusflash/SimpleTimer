package com.optimus.simpletimer.recievers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.optimus.simpletimer.helpers.TimerState
import com.optimus.simpletimer.services.TimerService

/**
 * Created by Dmitriy Chebotar on 05.03.2020.
 */
class TimerReceiver(private val listener: (timeInFuture: Long, state: TimerState) -> Unit) :
    BroadcastReceiver() {

    companion object {
        const val MILLISECONDS_EXTRA_START = "milliseconds_extra_start"
    }

    private var millisInFuture = 0L

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            when (intent.action) {
                TimerService.BROADCAST_ACTION_START -> {
                    millisInFuture = intent.getLongExtra(MILLISECONDS_EXTRA_START, 0)
                    listener.invoke(millisInFuture, TimerState.STARTED)
                }
                TimerService.BROADCAST_ACTION_PAUSE ->{
                    listener.invoke(millisInFuture, TimerState.PAUSED)
                }
                TimerService.BROADCAST_ACTION_STOP ->{
                    listener.invoke(0, TimerState.STOPPED)
                }
            }
        }
    }
}