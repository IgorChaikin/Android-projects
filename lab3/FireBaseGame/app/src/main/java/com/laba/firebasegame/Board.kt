package com.laba.firebasegame

import com.squareup.moshi.JsonClass
import java.io.Serializable
import kotlin.random.Random

const val SHIPS_COUNT = 10
const val BOARD_SIZE = 10

@JsonClass(generateAdapter = true)
data class Board(var isPlayersBoard: Boolean, var changesAllowed: Boolean) : Serializable{
    var table: List<List<Cell>> =
        List(BOARD_SIZE) { List(BOARD_SIZE) { Cell(CellState.EMPTY) } }

    init {
        var createdShips = 0
        while (createdShips < SHIPS_COUNT) {
            val x = Random.nextInt(0, BOARD_SIZE)
            val y = Random.nextInt(0, BOARD_SIZE)
            if (table[x][y].state != CellState.OK_SHIP) {
                table[x][y].state = CellState.OK_SHIP
                createdShips++
            }
        }

    }

}
