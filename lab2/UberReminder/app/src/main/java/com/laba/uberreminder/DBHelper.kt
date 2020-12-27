package com.laba.uberreminder

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.*

const val TABLE_NAME = "timer_sequence"

class DBHelper(context: Context) : SQLiteOpenHelper(context, "db", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table $TABLE_NAME (id integer primary key autoincrement, sequence blob)")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
    }

    fun addSequence(sequence: TimerSequence) {
        var bytes: ByteArray? = null
        val bos = ByteArrayOutputStream()
        try {
            val oos = ObjectOutputStream(bos)
            oos.writeObject(sequence)
            oos.flush()
            oos.close()
            bos.close()
            bytes = bos.toByteArray()
            writableDatabase.insert(
                TABLE_NAME,
                null,
                ContentValues().apply {
                    put("sequence", bytes)
                })
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
    }

    fun getAllSequences(): List<TimerSequence> {
        val c = writableDatabase.query(TABLE_NAME, null, null, null, null, null, null)
        return if (c.moveToFirst()) {
            val idColIndex = c.getColumnIndex("id")
            val sequenceColIndex = c.getColumnIndex("sequence")
            val sequences = mutableListOf<TimerSequence>()
            do {
                val id = c.getInt(idColIndex)
                val blobSequence = c.getBlob(sequenceColIndex)
                val sequenceReader = ObjectInputStream(ByteArrayInputStream(blobSequence))
                val sequence = sequenceReader.readObject() as TimerSequence
                sequences.add(sequence)
            } while (c.moveToNext())
            sequences
        } else emptyList()
    }

    fun getSequencesWithIds(): List<Pair<TimerSequence, Int>> {
        val c = writableDatabase.query(TABLE_NAME, null, null, null, null, null, null)
        return if (c.moveToFirst()) {
            val idColIndex = c.getColumnIndex("id")
            val sequenceColIndex = c.getColumnIndex("sequence")
            val sequences = mutableListOf<Pair<TimerSequence, Int>>()
            do {
                val id = c.getInt(idColIndex)
                val blobSequence = c.getBlob(sequenceColIndex)
                val sequenceReader = ObjectInputStream(ByteArrayInputStream(blobSequence))
                val sequence = sequenceReader.readObject() as TimerSequence
                sequences.add(Pair(sequence, id))
            } while (c.moveToNext())
            sequences
        } else emptyList()
    }

    fun deleteSequence(order: Int) {
        val sequences = getSequencesWithIds()
        writableDatabase.delete(TABLE_NAME, "id = ?", arrayOf(sequences[order].second.toString()))
    }

    fun updateSequence(order: Int, sequence: TimerSequence) {
        val sequences = getSequencesWithIds()
        var bytes: ByteArray? = null
        val bos = ByteArrayOutputStream()
        val oos = ObjectOutputStream(bos)
        oos.writeObject(sequence)
        oos.flush()
        oos.close()
        bos.close()
        bytes = bos.toByteArray()
        writableDatabase.update(TABLE_NAME, ContentValues().apply {
            put("sequence", bytes)
        }, "id = ?", arrayOf(sequences[order].second.toString()))
    }

    fun clearData(){
        writableDatabase.execSQL("delete from $TABLE_NAME")
    }
}
