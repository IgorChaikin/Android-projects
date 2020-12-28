package com.laba.uberreminder

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddOrEditViewModel : ViewModel() {
    private val _currentColor = MutableLiveData(0)
    val currentColor: LiveData<Int>
        get() = _currentColor
    private lateinit var repository: SequencesRepository

    private val _timers = MutableLiveData(listOf<NamedTimer>())
    fun add(sequence: TimerSequence) {
        repository.add(sequence)
    }

    val timers: LiveData<List<NamedTimer>>
        get() = _timers

    fun update(sequence: TimerSequence, recyclerOrder: Int) {
        repository.update(sequence, recyclerOrder)
    }

    fun postColor(colorId: Int) {
        _currentColor.postValue(colorId)
    }

    fun addTimer(timer: NamedTimer) {
        val oldTimers = _timers.value?.toMutableList()
        oldTimers?.add(timer)
        _timers.postValue(oldTimers)
    }

    fun postTimers(timers: List<NamedTimer>){
        _timers.postValue(timers)
    }

    fun updateTimer(position: Int, timer: NamedTimer){
        val oldTimers = _timers.value?.toMutableList()
        oldTimers?.set(position, timer)
        _timers.postValue(oldTimers)
    }

    fun deleteTimer(position: Int){
        val oldTimers = _timers.value?.toMutableList()
        oldTimers?.removeAt(position)
        _timers.postValue(oldTimers)
    }

    fun initViewModel(context: Context){
        repository = SequencesRepository(context)
    }
}
