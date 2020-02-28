package com.optimus.simpletimer.activities

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.optimus.simpletimer.R
import com.optimus.simpletimer.fragments.TimerDialogFragment
import com.optimus.simpletimer.helpers.PreferenceUtil
import com.optimus.simpletimer.helpers.TimeUtil
import com.optimus.simpletimer.helpers.TimerState
import com.optimus.simpletimer.services.TimerService
import com.optimus.simpletimer.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(),
    TimerDialogFragment.OnTimeChangeListener {

    private lateinit var mainViewModel: MainViewModel
    private var timerState = TimerState.STOPPED
    private var timeInMillis = 0L
    private var isStarted = false
    private var startHours = 0
    private var startMinutes = 0
    private var startSeconds = 0
    private var progressMaxValue = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        initViewModel()
    }

    private fun initViews() {
        val placeholder = resources.getString(R.string.time_placeholder)
        tv_timer_value.setOnClickListener {
            if (timerState == TimerState.STOPPED) {
                val dialog = TimerDialogFragment()
                dialog.show(supportFragmentManager, TimerDialogFragment.TAG)
            }
        }

        btn_timer_start.setOnClickListener {
            if (!isStarted) {
                if (tv_timer_value.text == placeholder) {
                    showToast("Please, set the time...")
                    return@setOnClickListener
                }
                mainViewModel.startTimer()
            } else {
                mainViewModel.pauseTimer()
            }
        }
        btn_timer_stop.setOnClickListener {
            mainViewModel.resetTimer()
        }
    }

    private fun startTimerService() {
        timeInMillis = mainViewModel.getTimeInMillis()
        Log.e("M_MainActivity", "startTimerService timeInMillis $timeInMillis")
        val intent = Intent(this, TimerService::class.java)
        intent.putExtra(TimerService.EXTRA_MESSAGE, timeInMillis)
        startService(intent)
    }

    private fun stopTimerService() {
        timeInMillis = mainViewModel.getTimeInMillis()
        Log.e("M_MainActivity", "stopTimerService timeInMillis $timeInMillis")
        val intent = Intent(this, TimerService::class.java)
        stopService(intent)
    }


    private fun initViewModel() {
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mainViewModel.getTime().observe(this, Observer {
            startHours = it.first
            startMinutes = it.second
            startSeconds = it.third
            updateTimerValue()
        })

        mainViewModel.getIsFinished().observe(this, Observer {
            if (it) {
                timerState = TimerState.STOPPED
                showToast("Finished...")
                updateButtons()
            }
        })

        mainViewModel.getStep().observe(this, Observer {
            progress_bar.progress = it
        })

        mainViewModel.getTimerState().observe(this, Observer {
            timerState = it
            updateButtons()
        })
    }


    override fun onTimeSet(hours: Int, minutes: Int, seconds: Int) {
        startHours = hours
        startMinutes = minutes
        startSeconds = seconds
        mainViewModel.setupTimer(hours, minutes, seconds)
        progressMaxValue = mainViewModel.getTimeInSeconds()
        Log.e("M_MainActivity", "onTimeSet: progressMaxValue $progressMaxValue")
        updateProgress()
    }

    private fun updateTimerValue() {
        val hoursPattern = if (startHours < 10) "0%d" else "%d"
        val minutesPattern = if (startMinutes < 10) "0%d" else "%d"
        val secondsPattern = if (startSeconds < 10) "0%d" else "%d"

        val result = "${String.format(hoursPattern, startHours)}:${String.format(
            minutesPattern,
            startMinutes
        )}:${String.format(secondsPattern, startSeconds)}"
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
                progress_bar.progress = 0
            }
        }
    }

    private fun showToast(message: String) {
        val offset100 = resources.getDimension(R.dimen.toast_offset_100).toInt()
        val offset50 = resources.getDimension(R.dimen.toast_offset_50).toInt()

        val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            toast.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM, 0, offset100)
        } else {
            toast.setGravity(Gravity.END or Gravity.BOTTOM, offset100, offset50)
        }
        toast.show()
    }

    private fun updateProgress() {
        progress_bar.max = progressMaxValue
        progress_bar.progress = progressMaxValue
    }


}




