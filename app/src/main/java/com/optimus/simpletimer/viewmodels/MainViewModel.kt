package com.optimus.simpletimer.viewmodels

import android.animation.ValueAnimator
import android.util.Log
import android.view.animation.LinearInterpolator
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.optimus.simpletimer.extensions.default
import com.optimus.simpletimer.extensions.set
import com.optimus.simpletimer.helpers.LiveDataManager
import com.optimus.simpletimer.helpers.TimeUnits
import com.optimus.simpletimer.helpers.TimeUtil
import com.optimus.simpletimer.helpers.TimeUtil.parseToHMS
import com.optimus.simpletimer.helpers.TimeUtil.parseToMillis
import com.optimus.simpletimer.helpers.TimerState
import com.optimus.simpletimer.model.SimpleTimer


/**
 * Created by Dmitriy Chebotar on 16.02.2020.
 */

class MainViewModel : ViewModel() {

    private val timerState = MutableLiveData<TimerState>().default(TimerState.STOPPED)
    private val time = LiveDataManager.timeMLD
    private val animationProperty = MutableLiveData<Int>().default(0)

    private var timeInMillis = 0L
    private var timer: SimpleTimer? = null
    private val valueAnimator = ValueAnimator()

    fun setupTimer(hours: Int, minutes: Int, seconds: Int) {
        time.set(Triple(hours, minutes, seconds))
    }

    fun getTimerState(): LiveData<TimerState> {
        return timerState
    }

    fun getTime(): LiveData<Triple<Int, Int, Int>> {
        return time
    }

    fun getAnimationProperty(): LiveData<Int>{
        return animationProperty
    }

    fun updateTime(milliseconds: Long){
        time.set(parseToHMS(milliseconds))
    }

    fun startTimer() {
//        timeInMillis = parseToMillis(time.value)
//        valueAnimator.setIntValues(timeInMillis.toInt(),0)
//        timer = SimpleTimer(timeInMillis + TimeUnits.SECOND.value) { millisInFuture ->
//            time.set(parseToHMS(millisInFuture))
//            if (millisInFuture < 1000) {
//                timerState.set(TimerState.STOPPED)
//            }
//        }
//        timer?.start()
//        startAnimation()
        timeInMillis = parseToMillis(time.value)
        valueAnimator.setIntValues(timeInMillis.toInt(),0)
        timerState.set(TimerState.STARTED)
        startAnimation()
    }

    private fun startAnimation() {
        valueAnimator.addUpdateListener {
            animationProperty.set(it.animatedValue as Int)
            Log.e("M_MainViewModel", "animationProperty ${animationProperty.value}")
        }
        valueAnimator.duration = timeInMillis
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.start()
    }

    fun pauseTimer() {
//        timeInMillis = parseToMillis(time.value)
//        timer?.pause()
        timerState.set(TimerState.PAUSED)
    }

    fun resetTimer() {
//        timer?.reset()
//        time.set(Triple(0, 0, 0))
        timerState.set(TimerState.STOPPED)
    }

}