package com.laba.uberreminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.laba.uberreminder.databinding.FragmentSequencesListBinding

class SequencesListFragment : Fragment(), TimerSequenceActionHandler {
    private val viewModel: SequencesListViewModel by viewModels()

    private var adapter: SequencesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initViewModel(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = SequencesAdapter(this).apply {
            data = viewModel.getSequences()
        }
        val binding = FragmentSequencesListBinding.inflate(layoutInflater, container, false)
        binding.mainRecycler.adapter = adapter
        binding.mainRecycler.layoutManager = LinearLayoutManager(context)
        binding.addSequenceButton.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.main_container, AddOrEditSequenceFragment())
                ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)?.addToBackStack(null)
                ?.commit()
        }
        return binding.root
    }

    override fun handle(position: Int, action: TimerSequenceAction) {
        if (action == TimerSequenceAction.DELETE) {
            viewModel.delete(position)
            adapter?.data = viewModel.getSequences()
        } else if (action == TimerSequenceAction.EDIT) {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.main_container, AddOrEditSequenceFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ORDER_KEY, position)
                        putSerializable(SEQUENCE_KEY, adapter?.data?.get(position))
                    }
                })
                ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                ?.addToBackStack(null)
                ?.commit()
        } else if (action == TimerSequenceAction.VIEW) {
            activity?.supportFragmentManager?.beginTransaction()
                ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                ?.replace(R.id.main_container, SequenceDetailsFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(SEQUENCE_KEY, viewModel.getSequences()[position])
                    }
                })?.addToBackStack(null)
                ?.commit()
        }
    }
}
