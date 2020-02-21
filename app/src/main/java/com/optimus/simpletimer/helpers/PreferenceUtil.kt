package com.optimus.simpletimer.helpers

import android.content.Context
import androidx.preference.PreferenceManager


/**
 * Created by Dmitriy Chebotar on 21.02.2020.
 */
object PreferenceUtil {
    private const val TIMER_STATE = "com.optimus.simpletimer.timer_state"

    fun saveCurrentState(context: Context, timerState: TimerState) {
        val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
        editor.putInt(TIMER_STATE, timerState.ordinal)
        editor.apply()
    }

    fun getCurrentState(context: Context): TimerState {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val stateOrdinal = preferences.getInt(TIMER_STATE, TimerState.STOPPED.ordinal)
        return when (stateOrdinal) {
            0 -> TimerState.STARTED
            1 -> TimerState.PAUSED
            else -> TimerState.STOPPED
        }
    }
}