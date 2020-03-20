package com.optimus.simpletimer.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Dmitriy Chebotar on 20.03.2020.
 */

@Entity(tableName = "timer")
data class TimerData(
    @PrimaryKey
    val id: Int,
    val timerState: Int,
    val time: Long,
    val progressMax: Int,
    val currentProgress: Long
) {
}