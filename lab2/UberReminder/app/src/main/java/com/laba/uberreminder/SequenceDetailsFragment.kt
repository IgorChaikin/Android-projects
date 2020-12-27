package com.laba.uberreminder

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.laba.uberreminder.databinding.FragmentSequenceDetailsBinding

class SequenceDetailsFragment : Fragment() {
    private lateinit var binding: FragmentSequenceDetailsBinding
    private lateinit var sequence: TimerSequence
    private var play = true
    private val viewModel: SequenceDetailsViewModel by viewModels()
    private var wasPause = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSequenceDetailsBinding.inflate(inflater, container, false)
        val argumentsSequence = arguments?.getSerializable(SEQUENCE_KEY) as TimerSequence?
        if (argumentsSequence == null) activity?.supportFragmentManager?.popBackStack()
        else sequence = argumentsSequence
        binding.name.text = sequence.name
        binding.root.setBackgroundColor(sequence.color)
        binding.timersRecycler.adapter = NamedTimerViewAdapter().apply {
            data = sequence.timers
        }
        binding.timersRecycler.layoutManager = LinearLayoutManager(context)
        binding.playButton.setOnClickListener {
            if (play) {
                activity?.startForegroundService(
                    Intent(
                        context,
                        TimerSequenceService::class.java
                    ).apply {
                        putExtra(SEQUENCE_KEY, sequence)
                        putExtra(
                            ACTION_KEY,
                            if (!wasPause) StartServiceAction.START else StartServiceAction.RESUME
                        )
                    })
                binding.playButton.setImageResource(R.drawable.ic_baseline_pause_24)
            } else {
                activity?.startForegroundService(
                    Intent(
                        context,
                        TimerSequenceService::class.java
                    ).apply {
                        putExtra(SEQUENCE_KEY, sequence)
                        putExtra(ACTION_KEY, StartServiceAction.PAUSE)
                    })
                wasPause = true
                binding.playButton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            }
            play = !play
        }
        binding.nextButton.setOnClickListener {
            activity?.startForegroundService(
                Intent(
                    context,
                    TimerSequenceService::class.java
                ).apply {
                    putExtra(SEQUENCE_KEY, sequence)
                    putExtra(ACTION_KEY, StartServiceAction.NEXT)
                })
        }
        binding.previousButton.setOnClickListener {
            activity?.startForegroundService(
                Intent(
                    context,
                    TimerSequenceService::class.java
                ).apply {
                    putExtra(SEQUENCE_KEY, sequence)
                    putExtra(ACTION_KEY, StartServiceAction.PREVIOUS)
                })
        }
        return binding.root
    }

    override fun onDetach() {
        activity?.stopService(Intent(context, TimerSequenceService::class.java))
        super.onDetach()
    }
}
