package com.optimus.simpletimer.services

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.optimus.simpletimer.App.Companion.CHANNEL_ID
import com.optimus.simpletimer.R
import com.optimus.simpletimer.activities.MainActivity
import com.optimus.simpletimer.helpers.TimeUnits
import com.optimus.simpletimer.helpers.TimerNotification
import com.optimus.simpletimer.model.SimpleTimer
import com.optimus.simpletimer.recievers.TimerReceiver

/**
 * Created by Dmitriy Chebotar on 18.02.2020.
 */
class TimerService : Service() {
    private lateinit var timer: SimpleTimer
    private var timeInMillis = 0L

    companion object {
        const val EXTRA_MESSAGE_START = "extra_message_start"
        const val BROADCAST_ACTION_START = "com.optimus.simpletimer.broadcast_action_start"
        const val BROADCAST_ACTION_STOP = "com.optimus.simpletimer.broadcast_action_stop"
        const val BROADCAST_ACTION_PAUSE = "com.optimus.simpletimer.broadcast_action_pause"
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            startForeground(TimerNotification.NOTIFICATION_ID, TimerNotification.getNotification(this))
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            if (it.action == MainActivity.ACTION_START) {
                timeInMillis = it.getLongExtra(EXTRA_MESSAGE_START, 0)
                val actionStartIntent = Intent(BROADCAST_ACTION_START)
                timer = SimpleTimer(timeInMillis + TimeUnits.SECOND.value) { millisInFuture ->
                    timeInMillis = millisInFuture
                    actionStartIntent.putExtra(
                        TimerReceiver.MILLISECONDS_EXTRA_START,
                        millisInFuture
                    )
                    sendBroadcast(actionStartIntent)
                    if (millisInFuture < 1000) {
                        stopSelf()
                    }
                }
                timer.start()
            }
            if (it.action == MainActivity.ACTION_PAUSE) {
                timer.pause()
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
        timer.reset()
        val actionStopIntent = Intent()
        actionStopIntent.action = BROADCAST_ACTION_STOP
        sendBroadcast(actionStopIntent)
        super.onDestroy()
    }
}