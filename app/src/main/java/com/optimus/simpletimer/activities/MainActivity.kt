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
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.optimus.simpletimer.R
import com.optimus.simpletimer.fragments.TimerDialogFragment
import com.optimus.simpletimer.helpers.TimeUtil.parseToMillis
import com.optimus.simpletimer.helpers.TimerState
import com.optimus.simpletimer.recievers.TimerReceiver
import com.optimus.simpletimer.services.TimerService
import com.optimus.simpletimer.services.TimerService.Companion.BROADCAST_ACTION_PAUSE
import com.optimus.simpletimer.services.TimerService.Companion.BROADCAST_ACTION_START
import com.optimus.simpletimer.services.TimerService.Companion.BROADCAST_ACTION_STOP
import com.optimus.simpletimer.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(),
    TimerDialogFragment.OnTimeChangeListener {

    companion object {
        const val ACTION_START = "action_start"
        const val ACTION_PAUSE = "action_pause"
        const val ACTION_START_FOREGROUND = "action_start_foreground"
        const val ACTION_STOP_FOREGROUND = "action_stop_foreground"
        private const val SPARE_SECOND = 999L
    }

    private lateinit var timerReceiver: TimerReceiver
    private lateinit var localBroadcastManager: LocalBroadcastManager

    private lateinit var mainViewModel: MainViewModel
    private lateinit var timerState: TimerState
    private var timeInMilliseconds = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        initViewModel()
    }

    override fun onStart() {
        super.onStart()
        Log.e("M_MainActivity", "onStart")
        mainViewModel.getData()
        initBroadcastReceiver()
        val intent = Intent(this, TimerService::class.java)
        intent.action = ACTION_STOP_FOREGROUND
        startService(intent)
    }

    override fun onStop() {
        super.onStop()
        Log.e("M_MainActivity", "onStop")
        mainViewModel.saveData()
        localBroadcastManager.unregisterReceiver(timerReceiver)
        if (timerState == TimerState.STARTED) {
            val intent = Intent(this, TimerService::class.java)
            intent.action = ACTION_START_FOREGROUND
            startService(intent)
        }
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
        localBroadcastManager = LocalBroadcastManager.getInstance(this)
        timerReceiver = TimerReceiver(mainViewModel::updateTime)
        val filter = IntentFilter()
        filter.addAction(BROADCAST_ACTION_START)
        filter.addAction(BROADCAST_ACTION_STOP)
        filter.addAction(BROADCAST_ACTION_PAUSE)
        localBroadcastManager.registerReceiver(timerReceiver, filter)
    }

    private fun startTimer() {
        val intent = Intent(this, TimerService::class.java)
        intent.action = ACTION_START
        intent.putExtra(TimerService.EXTRA_MESSAGE_START, timeInMilliseconds)
        startService(intent)
    }

    private fun pauseTimer() {
        val intent = Intent(this, TimerService::class.java)
        intent.action = ACTION_PAUSE
        startService(intent)
    }

    private fun stopTimer() {
        val intent = Intent(this, TimerService::class.java)
        stopService(intent)
    }

    private fun initViewModel() {
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        mainViewModel.getTimerState().observe(this, Observer {
            timerState = it
            updateButtons()
        })

        mainViewModel.getProgressMax().observe(this, Observer {
            progress_bar.max = (it - SPARE_SECOND).toInt()
        })

        mainViewModel.getTime().observe(this, Observer {
            tv_timer_value.text = it.first
            timeInMilliseconds = it.second
            updateProgress(timeInMilliseconds)
        })
    }

    private fun updateProgress(step: Long) {
        val progressValue = (step- SPARE_SECOND).toInt()
        progress_bar.progress = progressValue
    }

    override fun onTimeSet(hours: Int, minutes: Int, seconds: Int) {
        mainViewModel.setupTimer(hours, minutes, seconds)
        timeInMilliseconds = parseToMillis(hours, minutes, seconds) +SPARE_SECOND
        mainViewModel.setProgressMax(timeInMilliseconds.toInt())
        progress_bar.progress = (timeInMilliseconds.toInt() - SPARE_SECOND).toInt()
    }

    private fun updateButtons() {
        val icon = if (timerState == TimerState.STARTED) {
            resources.getDrawable(R.drawable.ic_pause_black_24dp, theme)
        } else {
            resources.getDrawable(R.drawable.ic_play_arrow_black_24dp, theme)
        }

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




