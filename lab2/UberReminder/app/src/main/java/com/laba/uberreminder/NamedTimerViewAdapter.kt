package com.laba.uberreminder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.laba.uberreminder.databinding.NamedTimerViewLayoutBinding

class NamedTimerViewAdapter() :
    RecyclerView.Adapter<NamedTimerViewAdapter.NamedTimerViewViewHolder>() {
    class NamedTimerViewViewHolder(val binding: NamedTimerViewLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    var data = listOf<NamedTimer>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NamedTimerViewViewHolder {
        return NamedTimerViewViewHolder(
            NamedTimerViewLayoutBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: NamedTimerViewViewHolder, position: Int) {
        holder.binding.run {
            if(data[position].isRelax){
                name.text = root.context.getString(R.string.pause)
                info.text = "${data[position].length.toMillis()}ms"
            } else {
                name.text = data[position].name
                info.text = "${data[position].length.toMillis()}ms, ${data[position].repeatTimes} repetitions"
            }
        }
    }
}
