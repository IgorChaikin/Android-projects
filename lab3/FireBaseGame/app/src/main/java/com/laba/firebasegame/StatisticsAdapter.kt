package com.laba.firebasegame

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.laba.firebasegame.databinding.UserItemBinding

class StatisticsAdapter(val users: List<User>) : RecyclerView.Adapter<StatisticsAdapter.StatisticsViewHolder>() {
    class StatisticsViewHolder(val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticsViewHolder {
        return StatisticsViewHolder(UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return users.count()
    }

    override fun onBindViewHolder(holder: StatisticsViewHolder, position: Int) {
        holder.binding.name.text = users[position].nickname
        holder.binding.wins.text = users[position].wonGames.toString()
    }
}
