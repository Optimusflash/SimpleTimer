package com.optimus.simpletimer.helpers

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Dmitriy Chebotar on 21.02.2020.
 */
object TimeUtil {
    private val date = Date()
    private val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.ROOT)
    init {
        dateFormat.timeZone = TimeZone.getTimeZone("Europe")
    }

    fun parseToMillis(hours: Int, minutes: Int, seconds: Int) : Long {
        return TimeUnits.SECOND.toMillis(seconds) + TimeUnits.MINUTE.toMillis(minutes) + TimeUnits.HOUR.toMillis(
            hours
        )
    }

    fun parseHMStoString(hours: Int, minutes: Int, seconds: Int): String{
        val inMillis= parseToMillis(hours, minutes, seconds)
        date.time = inMillis
        return dateFormat.format(date)
    }

    fun parseMillisToString(milliseconds: Long): String{
        date.time = milliseconds
        return dateFormat.format(date)
    }
}


