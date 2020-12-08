package com.laba.uberreminder

import android.graphics.Color
import androidx.annotation.StringRes

enum class SequenceColor(val color: Int, @StringRes val friendlyName: Int) {
    RED(Color.RED, R.string.red),
    GREEN(Color.GREEN, R.string.green),
    BLUE(Color.BLUE, R.string.blue),
    WHITE(Color.WHITE, R.string.white),
    YELLOW(Color.YELLOW, R.string.yellow)
}
