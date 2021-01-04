package com.laba.firebasegame

import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class Cell(
    var state: CellState
) : Serializable
