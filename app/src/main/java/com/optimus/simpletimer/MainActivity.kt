package com.optimus.simpletimer

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), TimerDialogFragment.OnTimeChangeListener {

    private lateinit var countDownTimer: CountDownTimer
    private var timeInMills = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv_timer_value.setOnClickListener {
            val dialog = TimerDialogFragment()
            dialog.show(supportFragmentManager, TimerDialogFragment.TAG)
        }

        btn_timer_start.setOnClickListener {
            startTimer()
        }

        btn_timer_stop.setOnClickListener {
            stopTimer()
        }

    }

    private fun stopTimer() {
        countDownTimer.cancel()
        tv_timer_value.text = applicationContext.getString(R.string.time_placeholder)
        Toast.makeText(applicationContext, "cancel", Toast.LENGTH_LONG).show()
    }

    private fun startTimer() {
        val placeholder = this.getString(R.string.time_placeholder)
        if (tv_timer_value.text == placeholder) {
            Toast.makeText(this, "Please, set the time...", Toast.LENGTH_LONG).show()
            return
        } else {
            countDownTimer = object : CountDownTimer(timeInMills, TimeUnits.SECOND.value) {
                override fun onTick(millisUntilFinished: Long) {
                    Log.e("M_MainActivity", "$millisUntilFinished")
                    updateTime(millisUntilFinished)
                }
                override fun onFinish() {
                }
            }.start()
        }
    }


    override fun onTimeSet(hours: Int, minutes: Int, seconds: Int) {
        Log.e("M_MainActivity", "onTimeChange: $hours, $minutes, $seconds")
        timeInMills =
            TimeUnits.SECOND.toMillis(seconds) + TimeUnits.MINUTE.toMillis(minutes) + TimeUnits.HOUR.toMillis(
                hours
            )

        updateTime(timeInMills)

    }

    private fun updateTime(milliseconds: Long) {

        val hours = milliseconds / TimeUnits.HOUR.value
        val minutes = (milliseconds % TimeUnits.HOUR.value) / TimeUnits.MINUTE.value
        val seconds = (milliseconds % TimeUnits.MINUTE.value) / TimeUnits.SECOND.value

        val hoursPattern = if (hours < 10) "0%d" else "%d"
        val minutesPattern = if (minutes < 10) "0%d" else "%d"
        val secondsPattern = if (seconds < 10) "0%d" else "%d"

        val result = "${String.format(hoursPattern, hours)}:${String.format(
            minutesPattern,
            minutes
        )}:${String.format(secondsPattern, seconds)}"

        tv_timer_value.text = result
    }


}




