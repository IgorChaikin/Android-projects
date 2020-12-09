package com.laba.valuesconverter

import androidx.annotation.StringRes

enum class Measures(
    val category: Categories,
    val value: Int,
    @StringRes val friendlyNameResId: Int
) {
    MILLIMETER(Categories.LENGTH, 1, R.string.millimeter),
    METER(Categories.LENGTH, 1000, R.string.meter),
    KILOMETER(Categories.LENGTH, 1000000, R.string.kilometer),
    SECOND(Categories.TIME, 1, R.string.second),
    MINUTE(Categories.TIME, 60, R.string.minute),
    HOUR(Categories.TIME, 3600, R.string.hour),
    MILLILITER(Categories.VOLUME, 1, R.string.milliliter),
    LITER(Categories.VOLUME, 1000, R.string.liter),
    KILOLITER(Categories.VOLUME, 1000000, R.string.kiloliter)
}
