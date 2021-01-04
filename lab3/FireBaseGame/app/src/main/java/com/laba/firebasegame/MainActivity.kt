package com.laba.firebasegame

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.laba.firebasegame.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    val viewModel: GameViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        binding.stat.setOnClickListener {
            val intent = Intent(this, StatisticsActivity::class.java)
            startActivity(intent)
        }
        binding.profile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java).apply {
                putExtra("email", viewModel.email)
            }
            startActivity(intent)
        }
        viewModel.gameId.observe(this, Observer {
            if (it != null) {
                binding.gameId.setText(it)
            }
        })
        setContentView(binding.root)
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            14
        )
        viewModel.game.observe(this, Observer {
            supportFragmentManager.beginTransaction()
                .replace(R.id.my_board_container, BoardFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(BOARD_KEY, viewModel.game.value!!.gamersOneTable)
                    }
                }).commit()
            supportFragmentManager.beginTransaction()
                .replace(R.id.not_my_board_container, BoardFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(BOARD_KEY, viewModel.game.value!!.gamersTwoTable)
                    }
                }).commit()
        })
        viewModel.end.observe(this, Observer {
            if (it) {
                AlertDialog.Builder(this).setTitle("Game end")
                    .setMessage("gamer " + viewModel.game.value!!.winner!!.toString() + "won").create().show()
            }
        })
        viewModel.playerOneName.observe(this, Observer {
            binding.nameOne.setText(it)
        })
        viewModel.secondPlayerName.observe(this, Observer {
            binding.nameTwo.setText(it)
        })
    }

    fun showOtherGameAltert() {
        val input = EditText(this@MainActivity)
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        input.layoutParams = lp
        val alertDialog =
            AlertDialog.Builder(this).setTitle("Enter id").setPositiveButton("OK") { _, _ ->
                viewModel.joinGame(input.text.toString())
            }.setView(input).create()
        alertDialog.show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 14) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                viewModel.postUser(user!!)
                binding.joinOther.setOnClickListener {
                    showOtherGameAltert()
                }
            } else {
                finish()
            }
        }
    }

}
