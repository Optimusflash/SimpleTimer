package com.optimus.simpletimer.activities

import android.os.Bundle
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

    private var isStarted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        initViewModel()

    }

    private fun initViewModel() {
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mainViewModel.getTime().observe(this, Observer {
            updateViews(it.first, it.second, it.third)
        })
    }

    private fun initViews() {
        tv_timer_value.setOnClickListener {
            val dialog = TimerDialogFragment()
            dialog.show(supportFragmentManager, TimerDialogFragment.TAG)
        }

        btn_timer_start.setOnClickListener {
            if (!isStarted) {
                timerState = TimerState.START
                updateTimerState()
            } else {
                timerState = TimerState.PAUSE
                updateTimerState()
            }
        }

        btn_timer_stop.setOnClickListener {
            timerState = TimerState.STOP
            updateTimerState()
        }
    }

    override fun onTimeSet(hours: Int, minutes: Int, seconds: Int) {
        mainViewModel.setData(hours, minutes, seconds)
        updateViews(hours, minutes, seconds)
    }

    private fun updateViews(hours: Int, minutes: Int, seconds: Int) {

        val hoursPattern = if (hours < 10) "0%d" else "%d"
        val minutesPattern = if (minutes < 10) "0%d" else "%d"
        val secondsPattern = if (seconds < 10) "0%d" else "%d"

        val result = "${String.format(hoursPattern, hours)}:${String.format(
            minutesPattern,
            minutes
        )}:${String.format(secondsPattern, seconds)}"

        tv_timer_value.text = result
    }

    private fun updateTimerState() {
        val placeholder = applicationContext.getString(R.string.time_placeholder)
        val startIcon = resources.getDrawable(R.drawable.ic_play_arrow_black_24dp, theme)
        val pauseIcon = resources.getDrawable(R.drawable.ic_pause_black_24dp, theme)

        when (timerState) {
            TimerState.START -> {
                if (tv_timer_value.text == placeholder){
                    Toast.makeText(this, "Please, set the time...", Toast.LENGTH_SHORT).show()
                    return
                }
                btn_timer_stop.visibility = View.VISIBLE
                btn_timer_start.setImageDrawable(pauseIcon)
                isStarted = true
                mainViewModel.startTimer()
            }

            TimerState.PAUSE -> {
                btn_timer_start.setImageDrawable(startIcon)
                isStarted = false
                mainViewModel.pauseTimer()
            }

            TimerState.STOP -> {
                isStarted = false
                btn_timer_start.setImageDrawable(startIcon)
                tv_timer_value.text = placeholder
                btn_timer_stop.visibility = View.GONE
                mainViewModel.stopTimer()
            }

        }

    }

}




