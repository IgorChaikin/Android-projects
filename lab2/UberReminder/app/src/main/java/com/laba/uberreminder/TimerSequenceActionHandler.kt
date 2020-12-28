package com.laba.uberreminder

interface TimerSequenceActionHandler {
    fun handle(position: Int, action: TimerSequenceAction)
}
