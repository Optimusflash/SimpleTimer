package com.optimus.simpletimer.extensions

import androidx.lifecycle.MutableLiveData

/**
 * Created by Dmitriy Chebotar on 16.02.2020.
 */


fun <T> mutableLiveData(defaultValue: T? = null ): MutableLiveData<T> {
    val data = MutableLiveData<T>()
    if (defaultValue!=null) data.value = defaultValue
    return data
}