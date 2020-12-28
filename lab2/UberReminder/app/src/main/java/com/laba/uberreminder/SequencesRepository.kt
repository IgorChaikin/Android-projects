package com.laba.uberreminder

import android.app.Activity
import android.content.Context
import androidx.preference.PreferenceManager

class SequencesRepository(private val context: Context) {

    private val helper = DBHelper(context)

    fun getSequences(): List<TimerSequence> {
        return helper.getAllSequences()
    }

    fun delete(recyclerOrder: Int)
    {
        helper.deleteSequence(recyclerOrder)
    }

    fun add(sequence: TimerSequence){
        helper.addSequence(sequence)
    }

    fun update(sequence: TimerSequence, recyclerOrder: Int){
        helper.updateSequence(recyclerOrder, sequence)
    }

    fun clear(){
        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().apply()
        helper.clearData()
        (context as Activity).finish()
    }
}
