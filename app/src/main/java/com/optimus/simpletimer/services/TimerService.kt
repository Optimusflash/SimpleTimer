package com.optimus.simpletimer.services

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.optimus.simpletimer.App.Companion.CHANNEL_ID
import com.optimus.simpletimer.R
import com.optimus.simpletimer.activities.MainActivity
import com.optimus.simpletimer.model.SimpleTimer

/**
 * Created by Dmitriy Chebotar on 18.02.2020.
 */
class TimerService: Service() {
    private lateinit var timer: SimpleTimer
    private var timeInMillis = 0L

    companion object {
        const val EXTRA_MESSAGE = "message"
        const val NOTIFICATION_ID = 121212
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            timeInMillis = it.getLongExtra(EXTRA_MESSAGE,0)
        }

        val actionIntent = Intent(this, MainActivity::class.java)

        val actionPendingIntent = PendingIntent.getActivity(
            this,
            0,
            actionIntent,
            PendingIntent.FLAG_NO_CREATE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID )
            .setSmallIcon(R.drawable.ic_timer_black_24dp)
            .setContentTitle("Simple timer")
            .setContentText("Running...")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setAutoCancel(true)
            .setContentIntent(actionPendingIntent)
            .build()

//        timer = SimpleTimer(timeInMillis)
//        timer.start()

        startForeground(NOTIFICATION_ID,notification)

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }

}