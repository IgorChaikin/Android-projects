package com.laba.firebasegame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.laba.firebasegame.databinding.ActivityStatisticsBinding

class StatisticsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityStatisticsBinding.inflate(layoutInflater)
        val viewModel: StatisticsViewModel by viewModels()
        binding.statsRecycler.layoutManager = LinearLayoutManager(this)
        viewModel.loadUsers()
        viewModel.users.observe(this, Observer {
            binding.statsRecycler.adapter = StatisticsAdapter(it)
        })
        setContentView(binding.root)
    }
}
