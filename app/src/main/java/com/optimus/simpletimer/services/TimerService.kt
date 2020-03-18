package com.optimus.simpletimer.services

import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import com.optimus.simpletimer.activities.MainActivity
import com.optimus.simpletimer.helpers.TimerNotification
import com.optimus.simpletimer.recievers.TimerReceiver

/**
 * Created by Dmitriy Chebotar on 18.02.2020.
 */
class TimerService : Service() {
    private lateinit var timer: CountDownTimer
    private var timeInMillis = 0L

    companion object {
        const val EXTRA_MESSAGE_START = "extra_message_start"
        const val BROADCAST_ACTION_START = "com.optimus.simpletimer.broadcast_action_start"
        const val BROADCAST_ACTION_STOP = "com.optimus.simpletimer.broadcast_action_stop"
        const val BROADCAST_ACTION_PAUSE = "com.optimus.simpletimer.broadcast_action_pause"
    }

    override fun onCreate() {
        super.onCreate()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            startForeground(TimerNotification.NOTIFICATION_ID, TimerNotification.getNotification(this))
//        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            if (it.action == MainActivity.ACTION_START_FOREGROUND) {
                startForeground(
                    TimerNotification.NOTIFICATION_ID,
                    TimerNotification.getNotification(this)
                )
            }
            if (it.action == MainActivity.ACTION_STOP_FOREGROUND) {
                stopForeground(true)
            }
            if (it.action == MainActivity.ACTION_START) {
                timeInMillis = it.getLongExtra(EXTRA_MESSAGE_START, 0)
                val actionStartIntent = Intent(BROADCAST_ACTION_START)
                timer = object : CountDownTimer(timeInMillis+1000, 10) {
                    override fun onTick(millisUntilFinished: Long) {
                        Log.e("M_TimerService", "$millisUntilFinished")
                        timeInMillis = millisUntilFinished
                        actionStartIntent.putExtra(
                            TimerReceiver.MILLISECONDS_EXTRA_START,
                            millisUntilFinished
                        )
                        if (millisUntilFinished<1000){
                            stopSelf()
                        }
                        sendBroadcast(actionStartIntent)
                    }

                    override fun onFinish() {

                    }

                }
                timer.start()
            }
            if (it.action == MainActivity.ACTION_PAUSE) {
                timer.cancel()
                val actionPauseIntent = Intent(BROADCAST_ACTION_PAUSE)
                actionPauseIntent.putExtra(TimerReceiver.MILLISECONDS_EXTRA_PAUSE, timeInMillis)
                sendBroadcast(actionPauseIntent)
            }
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        Log.e("M_TimerService", "Service onDestroy")
        timer.cancel()
        val actionStopIntent = Intent()
        actionStopIntent.action = BROADCAST_ACTION_STOP
        sendBroadcast(actionStopIntent)
        super.onDestroy()
    }
}