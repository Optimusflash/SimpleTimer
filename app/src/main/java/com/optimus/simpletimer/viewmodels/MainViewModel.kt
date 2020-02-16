package com.optimus.simpletimer.viewmodels

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.optimus.simpletimer.extensions.mutableLiveData
import com.optimus.simpletimer.helpers.TimeUnits

/**
 * Created by Dmitriy Chebotar on 16.02.2020.
 */

class MainViewModel: ViewModel() {
    private lateinit var countDownTimer: CountDownTimer
    private val time = mutableLiveData(Triple(0,0,0))
    private var timeInMillis = 0L
    private val isFinished = mutableLiveData(false)


    fun setupTimer(hours: Int, minutes: Int, seconds: Int) {
        val millisTime =
            TimeUnits.SECOND.toMillis(seconds) + TimeUnits.MINUTE.toMillis(minutes) + TimeUnits.HOUR.toMillis(
                hours
            )
        timeInMillis = millisTime
        updateTime(timeInMillis)
    }

    fun getTime(): LiveData<Triple<Int,Int,Int>>{
        return time
    }

    fun getIsFinished(): LiveData<Boolean>{
        return isFinished
    }

    private fun updateTime(milliseconds: Long){

        val hours = milliseconds / TimeUnits.HOUR.value
        val minutes = (milliseconds % TimeUnits.HOUR.value) / TimeUnits.MINUTE.value
        val seconds = (milliseconds % TimeUnits.MINUTE.value) / TimeUnits.SECOND.value

        val triple = Triple(hours.toInt(), minutes.toInt(), seconds.toInt())
        time.value = triple

    }

    fun startTimer() {
        isFinished.value = false
        Log.e("M_MainViewModel", " startTimer")
            countDownTimer = object : CountDownTimer(timeInMillis, TimeUnits.SECOND.value) {
                override fun onTick(millisUntilFinished: Long) {
                    Log.e("M_MainViewModel", "$millisUntilFinished")
                    timeInMillis = millisUntilFinished
                    updateTime(millisUntilFinished)
                    if (millisUntilFinished<1000){
                        isFinished.value = true
                    }
                }

                override fun onFinish() {

                }
            }.start()
    }

    fun stopTimer(){
        countDownTimer.cancel()
        timeInMillis = 0L
        time.value = Triple(0,0,0)
    }

    fun pauseTimer() {
        countDownTimer.cancel()
    }

}