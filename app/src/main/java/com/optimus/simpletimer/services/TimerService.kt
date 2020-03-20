package com.optimus.simpletimer.services

import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import com.optimus.simpletimer.activities.MainActivity
import com.optimus.simpletimer.helpers.TimerNotification
import com.optimus.simpletimer.model.TimerData
import com.optimus.simpletimer.recievers.TimerReceiver
import com.optimus.simpletimer.repositories.MainRepository

/**
 * Created by Dmitriy Chebotar on 18.02.2020.
 */
class TimerService : Service() {
    private var timer: CountDownTimer? = null
    private var timeInMillis = 0L
    private val timerRepository = MainRepository()

    companion object {
        const val EXTRA_MESSAGE_START = "extra_message_start"
        const val BROADCAST_ACTION_START = "com.optimus.simpletimer.broadcast_action_start"
        const val BROADCAST_ACTION_STOP = "com.optimus.simpletimer.broadcast_action_stop"
        const val BROADCAST_ACTION_PAUSE = "com.optimus.simpletimer.broadcast_action_pause"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            if (it.action == MainActivity.ACTION_START) {
                timeInMillis = it.getLongExtra(EXTRA_MESSAGE_START, 0)
                val actionStartIntent = Intent(BROADCAST_ACTION_START)
                timer = object : CountDownTimer(timeInMillis, 16) {
                    override fun onTick(millisUntilFinished: Long) {
                        timeInMillis = millisUntilFinished
                        actionStartIntent.putExtra(
                            TimerReceiver.MILLISECONDS_EXTRA_START,
                            millisUntilFinished
                        )
                        sendBroadcast(actionStartIntent)
                        if (timeInMillis < 1000) {
                            this.onFinish()

                        }
                    }

                    override fun onFinish() {
                        stopSelf()
                    }
                }
                timer?.start()
            }
            if (it.action == MainActivity.ACTION_PAUSE) {
                timer?.cancel()
                val actionPauseIntent = Intent(BROADCAST_ACTION_PAUSE)
                sendBroadcast(actionPauseIntent)
            }
            if (it.action == MainActivity.ACTION_START_FOREGROUND) {
                startForeground(
                    TimerNotification.NOTIFICATION_ID,
                    TimerNotification.getNotification(this)
                )
            }
            if (it.action == MainActivity.ACTION_STOP_FOREGROUND) {
                stopForeground(true)
            }
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        Log.e("M_TimerService", "Service onDestroy")
        timer?.cancel()
        val actionStopIntent = Intent()
        actionStopIntent.action = BROADCAST_ACTION_STOP
        sendBroadcast(actionStopIntent)
        val subscribe = timerRepository.updateData(TimerData(1, 2, 0, 0, 0))
            .subscribe {
                Log.e("M_TimerService", "triggered")
            }
        super.onDestroy()
    }
}