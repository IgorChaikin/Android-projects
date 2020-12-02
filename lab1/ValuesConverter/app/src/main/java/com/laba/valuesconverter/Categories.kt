package com.laba.valuesconverter

import androidx.annotation.StringRes

enum class Categories(@StringRes val friendlyNameResId: Int) {
    LENGTH(R.string.length),
    TIME(R.string.time),
    VOLUME(R.string.volume)
}
