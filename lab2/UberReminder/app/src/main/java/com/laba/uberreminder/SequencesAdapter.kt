package com.laba.uberreminder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.laba.uberreminder.databinding.SequenceRecyclerItemBinding

class SequencesAdapter(private val sequenceActionHandler: TimerSequenceActionHandler) : RecyclerView.Adapter<SequencesAdapter.SequenceViewHolder>() {
    var data: List<TimerSequence> = emptyList()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    class SequenceViewHolder(val binding: SequenceRecyclerItemBinding, val sequenceActionHandler: TimerSequenceActionHandler) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SequenceViewHolder {
        return SequenceViewHolder(SequenceRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), sequenceActionHandler)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: SequenceViewHolder, position: Int) {
        holder.binding.run {
            name.text = data[position].name
            root.setCardBackgroundColor(data[position].color)
            editButton.setOnClickListener {
                sequenceActionHandler.handle(position, TimerSequenceAction.EDIT)
            }
            deleteButton.setOnClickListener {
                sequenceActionHandler.handle(position, TimerSequenceAction.DELETE)
            }
            root.setOnClickListener {
                sequenceActionHandler.handle(position, TimerSequenceAction.VIEW)
            }
        }
    }
}
