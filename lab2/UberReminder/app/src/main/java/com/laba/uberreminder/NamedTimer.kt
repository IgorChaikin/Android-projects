package com.laba.uberreminder

import java.io.Serializable
import java.time.Duration

class NamedTimer(
    val name: String,
    val length: Duration,
    val repeatTimes: Int,
    val isRelax: Boolean = false,
    val onPause: Boolean = false
) : Serializable
