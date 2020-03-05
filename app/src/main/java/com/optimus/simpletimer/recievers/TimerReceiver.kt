package com.optimus.simpletimer.recievers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

/**
 * Created by Dmitriy Chebotar on 05.03.2020.
 */
class TimerReceiver(private val listener: (timeInFuture: Long)->Unit) : BroadcastReceiver() {

    companion object{
        const val MILLISECONDS_EXTRA = "milliseconds_extra"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        //Toast.makeText(context, "Triggered", Toast.LENGTH_SHORT).show()
        val millisInFuture = intent?.getLongExtra(MILLISECONDS_EXTRA, 0)
        millisInFuture?.let {
            listener.invoke(it)
        }
    }
}