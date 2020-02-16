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
    private var timerState = TimerState.STOP
    private var placeholder = ""
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

        initViews()
        initViewModel()

        if (savedInstanceState != null) {
            isStarted = savedInstanceState.getBoolean(IS_RUNNING)
            timerState = savedInstanceState.getSerializable(TIMER_STATE) as TimerState
            Log.e("M_MainActivity", timerState.name)
            startHours = savedInstanceState.getInt(START_HOURS)
            startMinutes = savedInstanceState.getInt(START_MINUTES)
            startSeconds = savedInstanceState.getInt(START_SECONDS)
            updateView(startHours, startMinutes, startSeconds)
            updateUi()
        }

    }

    private fun initViewModel() {
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mainViewModel.getTime().observe(this, Observer {
            startHours = it.first
            startMinutes = it.second
            startSeconds = it.third

            Log.e("M_MainActivity", "initViewModel $startHours $startMinutes $startSeconds")
            updateView(hours = startHours, minutes = startMinutes, seconds = startSeconds)
        })
    }

    private fun initViews() {

        placeholder = this.getString(R.string.time_placeholder)

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
                timerState = TimerState.START
                updateUi()
                mainViewModel.startTimer()
            } else {
                timerState = TimerState.PAUSE
                updateUi()
                mainViewModel.pauseTimer()
            }
        }

        btn_timer_stop.setOnClickListener {
            timerState = TimerState.STOP
            updateUi()
            mainViewModel.stopTimer()
        }
    }

    override fun onTimeSet(hours: Int, minutes: Int, seconds: Int) {
        mainViewModel.setupTimer(hours, minutes, seconds)
        updateView(hours, minutes, seconds)
    }

    private fun updateView(hours: Int, minutes: Int, seconds: Int) {

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

    private fun updateUi() {
        val startIcon = resources.getDrawable(R.drawable.ic_play_arrow_black_24dp, theme)
        val pauseIcon = resources.getDrawable(R.drawable.ic_pause_black_24dp, theme)

        when (timerState) {
            TimerState.START -> {
                isStarted = true
                btn_timer_stop.visibility = View.VISIBLE
                btn_timer_start.setImageDrawable(pauseIcon)
            }

            TimerState.PAUSE -> {
                isStarted = false
                btn_timer_stop.visibility = View.VISIBLE
                btn_timer_start.setImageDrawable(startIcon)
            }

            TimerState.STOP -> {
                isStarted = false
                btn_timer_start.setImageDrawable(startIcon)
                btn_timer_stop.visibility = View.GONE
                startHours = 0
                startMinutes = 0
                startSeconds = 0
                Log.e("M_MainActivity", "Есть контакт")
                tv_timer_value.text = placeholder
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




