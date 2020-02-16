package com.optimus.simpletimer.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.optimus.simpletimer.R
import com.optimus.simpletimer.fragments.TimerDialogFragment
import com.optimus.simpletimer.helpers.TimerState
import com.optimus.simpletimer.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
    TimerDialogFragment.OnTimeChangeListener {

    private lateinit var mainViewModel: MainViewModel
    private var timerState = TimerState.STOPPED
    private var isStarted = false
    private var startHours = 0
    private var startMinutes = 0
    private var startSeconds = 0

    companion object {
        private const val IS_RUNNING = "isRunning"
        private const val TIMER_STATE = "timerState"
        private const val START_HOURS = "startHours"
        private const val START_MINUTES = "startMinutes"
        private const val START_SECONDS = "startSeconds"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews(savedInstanceState)
        initViewModel()
    }

    private fun initViews(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            isStarted = savedInstanceState.getBoolean(IS_RUNNING)
            timerState = savedInstanceState.getSerializable(TIMER_STATE) as TimerState
            Log.e("M_MainActivity", timerState.name)
            startHours = savedInstanceState.getInt(START_HOURS)
            startMinutes = savedInstanceState.getInt(START_MINUTES)
            startSeconds = savedInstanceState.getInt(START_SECONDS)
            updateTimerValue(startHours, startMinutes, startSeconds)
            updateButtons()
        }

        val placeholder = this.getString(R.string.time_placeholder)

        tv_timer_value.setOnClickListener {
            val dialog = TimerDialogFragment()
            dialog.show(supportFragmentManager, TimerDialogFragment.TAG)
        }

        btn_timer_start.setOnClickListener {
            if (!isStarted) {
                if (tv_timer_value.text == placeholder) {
                    Toast.makeText(this, "Please, set the time...", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                timerState = TimerState.STARTED
                updateButtons()
                mainViewModel.startTimer()
            } else {
                timerState = TimerState.PAUSED
                updateButtons()
                mainViewModel.pauseTimer()
            }
        }

        btn_timer_stop.setOnClickListener {
            timerState = TimerState.STOPPED
            updateButtons()
            mainViewModel.stopTimer()
        }
    }

    private fun initViewModel() {
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mainViewModel.getTime().observe(this, Observer {
            startHours = it.first
            startMinutes = it.second
            startSeconds = it.third

            Log.e("M_MainActivity", "initViewModel $startHours $startMinutes $startSeconds")
            updateTimerValue(hours = startHours, minutes = startMinutes, seconds = startSeconds)
        })

        mainViewModel.getIsFinished().observe(this, Observer {
            if (it) {
                timerState = TimerState.STOPPED
                updateButtons()
            }
        })
    }

    override fun onTimeSet(hours: Int, minutes: Int, seconds: Int) {
        startHours = hours
        startMinutes = minutes
        startSeconds = seconds
        mainViewModel.setupTimer(hours, minutes, seconds)
    }

    private fun updateTimerValue(hours: Int, minutes: Int, seconds: Int) {
        val hoursPattern = if (hours < 10) "0%d" else "%d"
        val minutesPattern = if (minutes < 10) "0%d" else "%d"
        val secondsPattern = if (seconds < 10) "0%d" else "%d"

        val result = "${String.format(hoursPattern, hours)}:${String.format(
            minutesPattern,
            minutes
        )}:${String.format(secondsPattern, seconds)}"
        Log.e("M_MainActivity", result)
        tv_timer_value.text = result
    }

    private fun updateButtons() {
        val icon = if (timerState == TimerState.STARTED) {
            resources.getDrawable(R.drawable.ic_pause_black_24dp, theme)
        } else {
            resources.getDrawable(R.drawable.ic_play_arrow_black_24dp, theme)
        }

        when (timerState) {
            TimerState.STARTED -> {
                isStarted = true
                btn_timer_stop.visibility = View.VISIBLE
                btn_timer_start.setImageDrawable(icon)
            }

            TimerState.PAUSED -> {
                isStarted = false
                btn_timer_stop.visibility = View.VISIBLE
                btn_timer_start.setImageDrawable(icon)
            }

            TimerState.STOPPED -> {
                isStarted = false
                btn_timer_stop.visibility = View.GONE
                btn_timer_start.setImageDrawable(icon)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_RUNNING, isStarted)
        outState.putSerializable(TIMER_STATE, timerState)
        outState.putInt(START_HOURS, startHours)
        outState.putInt(START_MINUTES, startMinutes)
        outState.putInt(START_SECONDS, startSeconds)
    }

}




