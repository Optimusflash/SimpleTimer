package com.optimus.simpletimer.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.optimus.simpletimer.di.App
import com.optimus.simpletimer.extensions.default
import com.optimus.simpletimer.extensions.set
import com.optimus.simpletimer.helpers.TimeUtil.parseHMStoString
import com.optimus.simpletimer.helpers.TimeUtil.parseMillisToString
import com.optimus.simpletimer.helpers.TimeUtil.parseToMillis
import com.optimus.simpletimer.helpers.TimerState
import com.optimus.simpletimer.model.TimerData
import com.optimus.simpletimer.repositories.MainRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


/**
 * Created by Dmitriy Chebotar on 16.02.2020.
 */

class MainViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    private val timerState = MutableLiveData<TimerState>().default(TimerState.STOPPED)
    private val time = MutableLiveData<Pair<String, Long>>().default(Pair("00:00:00", 0L))  //Time string, milliseconds
    private var progressMax = MutableLiveData<Int>().default(0)
    private val disposeBag = CompositeDisposable()

    fun setupTimer(hours: Int, minutes: Int, seconds: Int) {
        val timeString = parseHMStoString(hours, minutes, seconds)
        val timeInMillis = parseToMillis(hours, minutes, seconds)
        time.set(Pair(timeString, timeInMillis))
    }

    fun getTimerState(): LiveData<TimerState> {
        return timerState
    }

    fun getTime(): LiveData<Pair<String, Long>> {
        return time
    }

    fun getProgressMax(): LiveData<Int> {
        return progressMax
    }

    fun updateTime(milliseconds: Long, state: TimerState) {          //From BroadcastReceiver
        val timeString = parseMillisToString(milliseconds)
        when (state) {
            TimerState.STARTED -> {
                time.set((Pair(timeString, milliseconds)))
                timerState.set(TimerState.STARTED)
            }
            TimerState.PAUSED -> {
                time.set((Pair(timeString, milliseconds)))
                timerState.set(TimerState.PAUSED)
            }
            TimerState.STOPPED -> {
                time.set((Pair(timeString, milliseconds)))
                timerState.set(TimerState.STOPPED)
            }
        }
    }

    fun saveData() {
        val ordinalState = timerState.value!!.ordinal
        val currentProgress = time.value!!.second
        val currentTime = time.value!!.second
        val progressMax = progressMax.value!!
        val timerData = TimerData(1, ordinalState, currentTime, progressMax, currentProgress)
        val disposable = mainRepository.updateData(timerData).subscribe {
            Log.e("M_MainViewModel", "complete")
        }
        disposeBag.add(disposable)
    }

    fun getData() {
        val disposable = mainRepository.getData()
            .subscribe({
                Log.e("M_MainViewModel", "$it")
                handleTimerData(it)
            }, {
                Log.e("M_MainViewModel", "error")
            })
        disposeBag.add(disposable)
    }

    private fun handleTimerData(timerData: TimerData) {
        val timerState = when (timerData.timerState) {
            0 -> TimerState.STARTED
            1 -> TimerState.PAUSED
            else -> TimerState.STOPPED
        }
        val timeString = parseMillisToString(timerData.time)
        val timeInMillis = timerData.time
        val progressMax = timerData.progressMax

        this.timerState.set(timerState)
        this.progressMax.set(progressMax)
        time.set(Pair(timeString, timeInMillis))

    }

    fun setProgressMax(progressMax: Int) {
        this.progressMax.set(progressMax)
    }

    override fun onCleared() {
        disposeBag.dispose()
        super.onCleared()
    }
}