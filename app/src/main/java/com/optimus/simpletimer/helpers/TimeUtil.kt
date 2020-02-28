package com.optimus.simpletimer.helpers

/**
 * Created by Dmitriy Chebotar on 21.02.2020.
 */
object TimeUtil {
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
}