package com.optimus.simpletimer

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun toMillisTest(){
        val seconds = 3
        val minute = 59
        val hour = 24

        val millis1 = TimeUnits.SECOND.toMillis(seconds)
        assertEquals(millis1, 3000)

        val millis2 = TimeUnits.MINUTE.toMillis(minute)
        assertEquals(millis2, 3540000)

        val millis3 = TimeUnits.HOUR.toMillis(hour)
        assertEquals(millis3, 86400000)

        val sum = millis1 + millis2 + millis3
        assertEquals(sum, 89943000)
    }
    @Test
    fun toTimeUnitTest(){
        val milliseconds = 86400000L

        val result = TimeUnits.HOUR.toTimeUnit(milliseconds)
        assertEquals(result, 24)
        val result2 = TimeUnits.MINUTE.toTimeUnit(milliseconds)
        assertEquals(result, 1440)

    }


}
