package com.laba.valuesconverter

class ConvertingManager {
    fun convert(fromMeasure: Measures, toMeasure: Measures, value: Double): Double {
        return value * fromMeasure.value / toMeasure.value
    }
}
