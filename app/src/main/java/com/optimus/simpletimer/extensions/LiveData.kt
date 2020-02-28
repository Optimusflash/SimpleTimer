package com.optimus.simpletimer.extensions

import androidx.lifecycle.MutableLiveData

/**
 * Created by Dmitriy Chebotar on 16.02.2020.
 */

fun <T : Any?> MutableLiveData<T>.default(defaultValue: T?): MutableLiveData<T> = apply {
    value = defaultValue
}

fun <T> MutableLiveData<T>.set(newValue: T) = apply { value = newValue }