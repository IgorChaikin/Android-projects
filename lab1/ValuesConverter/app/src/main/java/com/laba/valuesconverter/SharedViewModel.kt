package com.laba.valuesconverter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class SharedViewModel : ViewModel() {
    private val convertingManager = ConvertingManager()

    private val _inputValue = MutableLiveData<String>("")
    val inputValue: LiveData<String>
        get() = _inputValue

    private val _currentCategory = MutableLiveData<Categories>(Categories.LENGTH)
    val currentCategory: LiveData<Categories>
        get() = _currentCategory

    private val _currentFromMeasure = MutableLiveData<Measures>()
    val currentFromMeasure: LiveData<Measures>
        get() = _currentFromMeasure

    private val _currentToMeasure = MutableLiveData<Measures>()
    val currentToMeasure: LiveData<Measures>
        get() = _currentToMeasure

    private val _outputValue = MutableLiveData<String>("")
    val outputValue: LiveData<String>
        get() = _outputValue

    fun postNumber(number: String) {
        var expression = _inputValue.value ?: ""
        if (!(number == "." && (_inputValue.value!!.contains(".") || _inputValue.value!!.isEmpty()))) {
            _inputValue.postValue(_inputValue.value!! + number)
            expression += number
        }

        convert(expression, _currentFromMeasure.value!!, _currentToMeasure.value!!)
    }

    private fun convert(expression: String, fromMeasure: Measures, toMeasure: Measures) {
        if (expression.isNotEmpty() && expression.last() != ',') {
            _outputValue.postValue(
                convertingManager.convert(
                    fromMeasure,
                    toMeasure,
                    expression.toDouble()
                ).toString()
            )
        } else _outputValue.postValue("")
    }


    fun postClear() {
        _inputValue.postValue("")
        _outputValue.postValue("")
    }

    fun postCategory(ordinal: Int) {
        _currentCategory.postValue(Categories.values()[ordinal])
        val expression = _inputValue.value ?: ""
        val measures = Measures.values().filter { it.ordinal == ordinal }
        convert(expression, measures[0], measures[0])
    }

    fun postMeasure(ordinal: Int, isFrom: Boolean) {
        val measure = Measures.values().filter {
            it.category.ordinal == _currentCategory.value?.ordinal ?: 0
        }[ordinal]
        (if (isFrom) _currentFromMeasure else _currentToMeasure).postValue(measure)
        val expression = _inputValue.value ?: ""
        if (isFrom) convert(expression, measure, _currentToMeasure.value ?: measure)
        else convert(expression, _currentFromMeasure.value ?: measure, measure)
    }

    fun swap() {
        val output = _outputValue.value ?: ""
        val fromMeasure = _currentFromMeasure.value!!
        val toMeasure = _currentToMeasure.value!!
        _currentToMeasure.postValue(fromMeasure)
        _currentFromMeasure.postValue(toMeasure)
        if (output.isNotEmpty()) {
            val doubleOutput = output.toDouble()
            _inputValue.postValue("%.4f".format(doubleOutput))
            convert("%.4f".format(doubleOutput), toMeasure, fromMeasure)
        } else {
            _inputValue.postValue("")
            _outputValue.postValue("")
        }
    }
}
