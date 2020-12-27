package com.laba.uberreminder

import java.io.Serializable

class TimerSequence(val name: String, val color: Int, val timers: List<NamedTimer>) : Serializable
