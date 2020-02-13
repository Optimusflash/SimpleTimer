package com.optimus.simpletimer

/**
 * Created by Dmitriy Chebotar on 13.02.2020.
 */
enum class TimeUnits(val value: Long) {
    SECOND(1000L),
    MINUTE(SECOND.value * 60),
    HOUR(MINUTE.value * 60);

    fun toMillis(value: Int): Long{
        return when(this){
            SECOND -> value.toLong() * this.value
            MINUTE -> value.toLong() * this.value
            HOUR -> value.toLong() * this.value
        }
    }

    fun toTimeUnit(timeInMillis: Long): Int{
        return (timeInMillis/this.value).toInt()
    }
}

