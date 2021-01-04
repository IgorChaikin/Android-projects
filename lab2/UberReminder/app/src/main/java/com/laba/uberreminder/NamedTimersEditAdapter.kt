package com.laba.uberreminder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.laba.uberreminder.databinding.NamedTimerEditLayoutBinding

class NamedTimersEditAdapter(private val timerSequenceActionHandler: TimerSequenceActionHandler) :
    RecyclerView.Adapter<NamedTimersEditAdapter.NamedTimerEditViewHolder>() {
    class NamedTimerEditViewHolder(val binding: NamedTimerEditLayoutBinding, val timerSequenceActionHandler: TimerSequenceActionHandler) :
        RecyclerView.ViewHolder(binding.root)

    var data = listOf<NamedTimer>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NamedTimerEditViewHolder {
        return NamedTimerEditViewHolder(
            NamedTimerEditLayoutBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            ), timerSequenceActionHandler
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: NamedTimerEditViewHolder, position: Int) {
        holder.binding.run {
            if(data[position].isRelax){
                name.text = root.context.getString(R.string.pause)
                info.text = "${data[position].length.toMillis()}ms"
            } else {
                name.text = data[position].name
                info.text = "${data[position].length.toMillis()}ms, ${data[position].repeatTimes} repetitions"
            }
            deleteButton.setOnClickListener {
                timerSequenceActionHandler.handle(position, TimerSequenceAction.DELETE)
            }
            editButton.setOnClickListener {
                timerSequenceActionHandler.handle(position, TimerSequenceAction.EDIT)
            }
        }
    }
}
