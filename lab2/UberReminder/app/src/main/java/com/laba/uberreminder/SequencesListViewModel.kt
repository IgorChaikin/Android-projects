package com.laba.uberreminder

import android.content.Context
import androidx.lifecycle.ViewModel

class SequencesListViewModel : ViewModel() {

    private lateinit var repository: SequencesRepository

    fun initViewModel(context: Context){
        repository = SequencesRepository(context)
    }

    fun getSequences(): List<TimerSequence> = repository.getSequences()
    fun delete(recyclerOrder: Int) = repository.delete(recyclerOrder)
}
