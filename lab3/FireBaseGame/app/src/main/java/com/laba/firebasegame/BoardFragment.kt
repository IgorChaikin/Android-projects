package com.laba.firebasegame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.laba.firebasegame.databinding.FragmentBoardBinding

const val BOARD_KEY = "board"

class BoardFragment : Fragment() {

    private lateinit var binding: FragmentBoardBinding
    private val viewModel: GameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBoardBinding.inflate(layoutInflater, container, false)
        binding.boardRecycler.layoutManager = GridLayoutManager(context, BOARD_SIZE)
        val board = requireArguments().getSerializable(BOARD_KEY)!! as Board
        binding.boardRecycler.adapter = BoardRecyclerAdapter(board) {
            //viewModel.game.value?.boom(it, board.isPlayersBoard)
            viewModel.boom(it, board.isPlayersBoard)
        }
        return binding.root
    }
}
