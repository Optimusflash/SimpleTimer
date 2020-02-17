package com.optimus.simpletimer.extensions

import android.content.Context

/**
 * Created by Dmitriy Chebotar on 17.02.2020.
 */


fun Context.dpToPx(dp: Int): Float {
    return dp.toFloat() * this.resources.displayMetrics.density
}

fun Context.pxToDp(px: Float): Float {
    return px / this.resources.displayMetrics.density
}