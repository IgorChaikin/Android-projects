package com.laba.firebasegame

import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
class Game(var firstUserEmail: String) : Serializable {
    var gamersOneTable = Board(isPlayersBoard = true, changesAllowed = false)
    var gamersTwoTable = Board(isPlayersBoard = false, changesAllowed = true)
    var gamerOneShipsKilled = 0
    var gamerTwoShipsKilled = 0
    var winner: Int? = null
    var secondUserEmail: String? = null

    fun boom(position: Int, isToMe: Boolean) {
        val board = if (gamersOneTable.isPlayersBoard == isToMe) gamersOneTable else gamersTwoTable
        val x = position % BOARD_SIZE
        val y = position / BOARD_SIZE
        gamersOneTable.changesAllowed = !gamersOneTable.changesAllowed
        gamersTwoTable.changesAllowed = !gamersTwoTable.changesAllowed
        when (board.table[x][y].state) {
            CellState.EMPTY -> {
                board.table[x][y].state = CellState.DAMAGED_EMPTY
            }
            CellState.OK_SHIP -> {
                if (gamersOneTable.isPlayersBoard == isToMe) gamerOneShipsKilled++ else gamerTwoShipsKilled++
                if(gamerOneShipsKilled == SHIPS_COUNT) {
                    winner = 2
                    gamersOneTable.changesAllowed = false
                    gamersTwoTable.changesAllowed = false
                }
                else if(gamerTwoShipsKilled == SHIPS_COUNT){
                    winner = 1
                    gamersOneTable.changesAllowed = false
                    gamersTwoTable.changesAllowed = false
                }
                board.table[x][y].state = CellState.DAMAGED_SHIP
            }
            CellState.DAMAGED_SHIP -> {
                board.table[x][y].state = CellState.DAMAGED_SHIP
            }
            CellState.DAMAGED_EMPTY -> {
                board.table[x][y].state = CellState.DAMAGED_EMPTY
            }
        }

    }
}
