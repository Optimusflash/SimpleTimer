package com.optimus.simpletimer.helpers

import androidx.lifecycle.MutableLiveData


/**
 * Created by Dmitriy Chebotar on 18.02.2020.
 */
object LiveDataManager {
    val timeMLD = MutableLiveData<Triple<Int, Int, Int>>()
    val isFinishedMLD = MutableLiveData<Boolean>()
    val stepMLD = MutableLiveData<Int>()
}
