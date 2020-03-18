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

    fun parseToHMS(milliseconds: Long): Triple<Int, Int, Int> {
        val hours = (milliseconds / TimeUnits.HOUR.value).toInt()
        val minutes = ((milliseconds % TimeUnits.HOUR.value) / TimeUnits.MINUTE.value).toInt()
        val seconds = ((milliseconds % TimeUnits.MINUTE.value) / TimeUnits.SECOND.value).toInt()
        return Triple(hours, minutes, seconds)
    }

    fun parseToMillis(time: Triple<Int, Int, Int>?) : Long {
        return if (time == null){
            0
        } else {
            val hours = time.first
            val minutes = time.second
            val seconds = time.third
            TimeUnits.SECOND.toMillis(seconds) + TimeUnits.MINUTE.toMillis(minutes) + TimeUnits.HOUR.toMillis(
                hours
            )
        }
    }

    fun parseToMillis(hours: Int, minutes: Int, seconds: Int) : Long {
        return TimeUnits.SECOND.toMillis(seconds) + TimeUnits.MINUTE.toMillis(minutes) + TimeUnits.HOUR.toMillis(
            hours
        )
    }

    fun parseToSeconds(hours: Int, minutes: Int, seconds: Int): Int{
        return TimeUnits.HOUR.toSeconds(hours) + TimeUnits.MINUTE.toSeconds(minutes) +TimeUnits.SECOND.toSeconds(seconds)
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


