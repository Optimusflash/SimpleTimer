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

    companion object {
        private const val IS_RUNNING = "isRunning"
        private const val TIMER_STATE = "timerState"
        private const val START_HOURS = "startHours"
        private const val START_MINUTES = "startMinutes"
        private const val START_SECONDS = "startSeconds"
        private const val PROGRESS_MAX_VALUE = "progressMaxValue"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews(savedInstanceState)
        initViewModel()
    }

    override fun onPause() {
        super.onPause()
        PreferenceUtil.saveCurrentState(this,timerState)

        when (timerState) {
            TimerState.STARTED -> {
                mainViewModel.pauseTimer()
                startTimerService()
            }
            TimerState.PAUSED -> {
                stopTimerService()
            }
            TimerState.STOPPED ->{
                stopTimerService()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        timerState = PreferenceUtil.getCurrentState(this)

        when(timerState){
            TimerState.STARTED->{
                stopTimerService()
                mainViewModel.startTimer()
                updateButtons()
            }

            TimerState.PAUSED ->{
                stopTimerService()
                mainViewModel.pauseTimer()
                updateButtons()
            }

            TimerState.STOPPED ->{
                stopTimerService()
                mainViewModel.resetTimer()
                updateButtons()
            }
        }
    }

    private fun initViews(savedInstanceState: Bundle?) {

        if (savedInstanceState != null) {
            isStarted = savedInstanceState.getBoolean(IS_RUNNING)
            timerState = savedInstanceState.getSerializable(TIMER_STATE) as TimerState
            Log.e("M_MainActivity", timerState.name)
            startHours = savedInstanceState.getInt(START_HOURS)
            startMinutes = savedInstanceState.getInt(START_MINUTES)
            startSeconds = savedInstanceState.getInt(START_SECONDS)
            progressMaxValue = savedInstanceState.getInt(PROGRESS_MAX_VALUE)
            Log.e("M_MainActivity", "Bundle: progressMaxValue $progressMaxValue")
            updateTimerValue(startHours, startMinutes, startSeconds)
            updateButtons()
            updateProgress()
        }

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

            updateTimerValue(hours = startHours, minutes = startMinutes, seconds = startSeconds)
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

    private fun updateTimerValue(hours: Int, minutes: Int, seconds: Int) {
        val hoursPattern = if (hours < 10) "0%d" else "%d"
        val minutesPattern = if (minutes < 10) "0%d" else "%d"
        val secondsPattern = if (seconds < 10) "0%d" else "%d"

        val result = "${String.format(hoursPattern, hours)}:${String.format(
            minutesPattern,
            minutes
        )}:${String.format(secondsPattern, seconds)}"
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_RUNNING, isStarted)
        outState.putSerializable(TIMER_STATE, timerState)
        outState.putInt(START_HOURS, startHours)
        outState.putInt(START_MINUTES, startMinutes)
        outState.putInt(START_SECONDS, startSeconds)
        outState.putInt(PROGRESS_MAX_VALUE, progressMaxValue)
    }

}




