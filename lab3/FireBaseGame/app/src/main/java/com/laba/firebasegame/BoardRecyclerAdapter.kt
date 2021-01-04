package com.laba.firebasegame

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.laba.firebasegame.databinding.CellItemBinding

class BoardRecyclerAdapter(val board: Board, val onClick: (position: Int) -> Unit) : RecyclerView.Adapter<BoardRecyclerAdapter.BoardViewHolder>() {
    class BoardViewHolder(val binding: CellItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        return BoardViewHolder(CellItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return BOARD_SIZE * BOARD_SIZE
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        val x = position % BOARD_SIZE
        val y = position / BOARD_SIZE
        if(board.isPlayersBoard || !board.changesAllowed)
            holder.binding.cellButton.isEnabled = false
        holder.binding.cellButton.setOnClickListener {
            onClick(position)
            notifyDataSetChanged()
        }
        when(board.table[x][y].state)
        {
            CellState.EMPTY -> {

            }
            CellState.OK_SHIP -> {
                if(board.isPlayersBoard)
                    holder.binding.cellButton.setImageResource(R.drawable.ic_historic_ship_24)
            }
            CellState.DAMAGED_SHIP -> {
                holder.binding.cellButton.setImageResource(R.drawable.ic_sinking_ship)
            }
            CellState.DAMAGED_EMPTY -> {
                holder.binding.cellButton.setImageResource(R.drawable.ic_fire_24)
            }
        }
    }
}
