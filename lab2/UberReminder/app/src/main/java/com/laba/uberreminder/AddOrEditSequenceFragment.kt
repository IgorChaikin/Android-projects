package com.laba.uberreminder

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.laba.uberreminder.databinding.AddTimerAlertDialogBinding
import com.laba.uberreminder.databinding.FragmentAddOrEditSequenceBinding
import java.time.Duration

const val ORDER_KEY = "ORDER"
const val SEQUENCE_KEY = "SEQUENCE"

class AddOrEditSequenceFragment : Fragment(), TimerSequenceActionHandler {

    private val viewModel: AddOrEditViewModel by viewModels()
    private var order: Int? = null
    private var sequence: TimerSequence? = null
    private lateinit var binding: FragmentAddOrEditSequenceBinding
    private var tempColor = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initViewModel(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddOrEditSequenceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        order = arguments?.getInt(ORDER_KEY)
        sequence = (arguments?.getSerializable(SEQUENCE_KEY) as TimerSequence?)
        if (order != null && sequence != null) {
            bindSequence()
        }
        viewModel.currentColor.observe(viewLifecycleOwner, Observer {
            binding.chooseColorSpinner.setText(SequenceColor.values()[it].friendlyName)
        })
        binding.chooseColorSpinner.setOnClickListener {
            showChooseColorDialog()
        }
        binding.goButton.setOnClickListener {
            val newSequence = takeSequence()
            if (order != null && sequence != null) {
                viewModel.update(newSequence, order!!)
            } else {
                viewModel.add(newSequence)
            }
            activity?.supportFragmentManager?.popBackStack()
        }
        binding.addTimerButton.setOnClickListener {
            showAddTimerDialog()
        }
        binding.addPauseButton.setOnClickListener {
            showAddTimerDialog(isPause = true)
        }
        binding.timersRecycler.adapter = NamedTimersEditAdapter(this)
        binding.timersRecycler.layoutManager = LinearLayoutManager(context)
        viewModel.timers.observe(viewLifecycleOwner, Observer {
            (binding.timersRecycler.adapter as NamedTimersEditAdapter).run {
                data = it
            }
            if (it.size >= 4) {
                binding.addTimerButton.isEnabled = false
                binding.addPauseButton.isEnabled = false
            } else {
                binding.addTimerButton.isEnabled = true
                binding.addPauseButton.isEnabled = true
            }
        })
    }

    private fun bindSequence() {
        binding.nameEditText.setText(sequence?.name)
        viewModel.postColor(SequenceColor.values().single { it.color == sequence?.color }.ordinal)
        viewModel.postTimers(sequence?.timers ?: emptyList())
    }

    private fun takeSequence(): TimerSequence {
        val name = binding.nameEditText.text.toString()
        val color = SequenceColor.values()
            .single {
                getString(it.friendlyName) == binding.chooseColorSpinner.text
            }.color
        return TimerSequence(name, color, viewModel.timers.value ?: emptyList())
    }

    private fun showChooseColorDialog() {
        AlertDialog.Builder(context).setTitle(requireContext().getString(R.string.choose_color))
            .setSingleChoiceItems(SequenceColor.values().map {
                getString(it.friendlyName)
            }.toTypedArray(), viewModel.currentColor.value ?: 0) { dialog, which ->
                tempColor = which
            }
            .setPositiveButton("OK") { dialog, which ->
                viewModel.postColor(tempColor)
            }
            .create()
            .show()
    }

    private fun showAddTimerDialog(
        isPause: Boolean = false,
        timer: NamedTimer? = null,
        position: Int? = null
    ) {
        val binding = AddTimerAlertDialogBinding.inflate(layoutInflater)
        if (isPause) {
            binding.name.visibility = View.GONE
            binding.repetitionsEditText.visibility = View.GONE
        }
        if (timer != null) {
            binding.name.setText(timer.name)
            binding.repetitionsEditText.setText(timer.repeatTimes.toString())
            binding.durationEditText.setText(timer.length.toMillis().toString())
        }
        AlertDialog.Builder(context).setTitle("Timer").setView(binding.root)
            .setPositiveButton("OK") { dialog, which ->
                if ((isPause && binding.durationEditText.text.isNotEmpty()) || (!isPause && binding.name.text.isNotEmpty()
                            && binding.repetitionsEditText.text.isNotEmpty()
                            && binding.durationEditText.text.isNotEmpty())
                ) {
                    val name = if (isPause) "" else binding.name.text.toString()
                    val repetitions =
                        if (isPause) 1 else binding.repetitionsEditText.text.toString().toInt()
                    val duration = binding.durationEditText.text.toString().toInt()
                    val newTimer = if (isPause) NamedTimer(
                        name,
                        Duration.ofMillis(duration.toLong()),
                        repetitions,
                        isRelax = true
                    )
                    else NamedTimer(name, Duration.ofMillis(duration.toLong()), repetitions)
                    if (timer == null) viewModel.addTimer(newTimer)
                    else if (position != null) viewModel.updateTimer(position, newTimer)
                }
            }
            .create()
            .show()
    }

    override fun handle(position: Int, action: TimerSequenceAction) {
        if (action == TimerSequenceAction.DELETE) {
            viewModel.deleteTimer(position)
        } else if (action == TimerSequenceAction.EDIT) {
            val timer = viewModel.timers.value?.get(position)
            val isPause = timer?.isRelax ?: false
            showAddTimerDialog(isPause, timer, position)
        }
    }
}
