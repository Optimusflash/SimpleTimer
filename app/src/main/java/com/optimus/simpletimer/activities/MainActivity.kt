package com.optimus.simpletimer.activities

import android.content.Intent
import android.content.IntentFilter
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
import com.optimus.simpletimer.helpers.TimeUtil
import com.optimus.simpletimer.helpers.TimeUtil.parseToMillis
import com.optimus.simpletimer.helpers.TimerNotification
import com.optimus.simpletimer.helpers.TimerState
import com.optimus.simpletimer.recievers.TimerReceiver
import com.optimus.simpletimer.services.TimerService
import com.optimus.simpletimer.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(),
    TimerDialogFragment.OnTimeChangeListener {

    companion object {
        private const val PROGRESS_BAR_MAX = "progress_bar_max"
    }

    private lateinit var timerReceiver: TimerReceiver

    private lateinit var mainViewModel: MainViewModel
    private lateinit var timerState: TimerState
    private var startHours = 0
    private var startMinutes = 0
    private var startSeconds = 0
    private var maxValue = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        initViewModel()
    }

    override fun onStart() {
        super.onStart()
        initBroadcastReceiver()
        //TODO: register receiver -> hide notification
        TimerNotification.hideNotification(context = this)
    }

    override fun onStop() {
        super.onStop()
        //TODO: unregister receiver -> show notification
        if (timerState == TimerState.STARTED){
            TimerNotification.showNotification(context = this)
        }
        unregisterReceiver(timerReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        maxValue = savedInstanceState.getInt(PROGRESS_BAR_MAX)
        progress_bar.max = maxValue
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(PROGRESS_BAR_MAX, maxValue)
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
            if (timerState != TimerState.STARTED) {
                if (tv_timer_value.text == placeholder) {
                    showToast("Please, set the time...")
                    return@setOnClickListener
                }
                startTimer()
            } else {
                pauseTimer()
            }
        }
        btn_timer_stop.setOnClickListener {
            stopTimer()
        }


    }

    private fun initBroadcastReceiver() {
        timerReceiver = TimerReceiver(mainViewModel::updateTime)
        val filter = IntentFilter()
        filter.addAction(TimerService.BROADCAST_ACTION_START)
        filter.addAction(TimerService.BROADCAST_ACTION_PAUSE)
        filter.addAction(TimerService.BROADCAST_ACTION_STOP)
        registerReceiver(timerReceiver, filter)
    }

    private fun startTimer() {
        val intent = Intent(this, TimerService::class.java)
        intent.action = TimerService.BROADCAST_ACTION_START
        intent.putExtra(
            TimerService.EXTRA_MESSAGE_START,
            parseToMillis(startHours, startMinutes, startSeconds)
        )
        startService(intent)
    }

    private fun pauseTimer() {
        val intent = Intent(this, TimerService::class.java)
        intent.action = TimerService.BROADCAST_ACTION_PAUSE
        startService(intent)
    }

    private fun stopTimer() {
        val intent = Intent(this, TimerService::class.java)
        stopService(intent)
    }

    private fun initViewModel() {
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        mainViewModel.getTimerState().observe(this, Observer {
            timerState = it
            updateButtons()
        })

        mainViewModel.getTime().observe(this, Observer {
            startHours = it.first
            startMinutes = it.second
            startSeconds = it.third
            updateTimerValue()
        })

        mainViewModel.getAnimationProperty().observe(this, Observer {
            Log.e("M_MainActivity", "maxValue $maxValue")
            progress_bar.progress = it
        })
    }

    override fun onTimeSet(hours: Int, minutes: Int, seconds: Int) {
        mainViewModel.setupTimer(hours, minutes, seconds)
        maxValue = TimeUtil.parseToMillis(hours, minutes, seconds).toInt()
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

        progress_bar.max = maxValue

        when (timerState) {
            TimerState.STARTED -> {
                btn_timer_stop.visibility = View.VISIBLE
                btn_timer_start.setImageDrawable(icon)
            }
            TimerState.PAUSED -> {
                btn_timer_stop.visibility = View.VISIBLE
                btn_timer_start.setImageDrawable(icon)
            }
            TimerState.STOPPED -> {
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
}




