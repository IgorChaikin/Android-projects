package com.laba.firebasegame

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class GameViewModel : ViewModel() {
    var db = FirebaseFirestore.getInstance()
    var game = MutableLiveData<Game>()
    private val moshi: Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    val gameId = MutableLiveData<String>()
    val end = MutableLiveData(false)
    val playerOneName = MutableLiveData<String>()
    val secondPlayerName = MutableLiveData<String>()
    var email: String? = null

    fun boom(position: Int, isToMe: Boolean) {
        game.value?.boom(position, isToMe)
        if (game.value!!.winner != null) {
            end.value = true
            val winnerEmail = if (game.value!!.winner!! == 1) game.value!!.firstUserEmail
            else game.value!!.secondUserEmail
            db.collection("users").whereEqualTo("email", winnerEmail).get().addOnSuccessListener {
                val user = it.documents[0].data
                user!!.set("won", (user["won"] as Long) + 1)
                it.documents[0].reference.update(user)
            }
        }
        updateGame()
    }

    fun updateGame() {
        val data = hashMapOf("data" to (moshi.adapter(Game::class.java).toJson(game.value)) as Any)
        db.collection("testGames").document(gameId.value!!).update(data)
    }


    fun postUser(user: FirebaseUser) {
        email = user.email
        db.collection("users").whereEqualTo("email", user.email).get().addOnSuccessListener {
            if (it.isEmpty) {
                playerOneName.value = user.email
                val newUser = hashMapOf<String, Any>(
                    "nickname" to user.email as Any,
                    "email" to user.email as Any,
                    "gravatar" to false,
                    "won" to 0
                )
                db.collection("users").add(newUser)
            } else {
                playerOneName.value = it.documents[0]["nickname"].toString()
            }
        }
        val newGame = Game(user.email.toString())
        val data = hashMapOf("data" to (moshi.adapter(Game::class.java).toJson(newGame)) as Any)
        game.postValue(newGame)
        db.collection("testGames").add(data)
            .addOnSuccessListener { documentReference ->
                gameId.postValue(documentReference.id)
                documentReference.addSnapshotListener { snapshot, e ->
                    val downloadedGame = moshi.adapter(Game::class.java)
                        .fromJson((snapshot!!.data!!["data"].toString()))!!
                    if (secondPlayerName.value == null) {
                        db.collection("users").whereEqualTo("email", downloadedGame.secondUserEmail)
                            .get().addOnSuccessListener {
                            if (!it.isEmpty)
                                secondPlayerName.value = it.documents[0]["nickname"].toString()
                        }
                    }
                    if (downloadedGame.gamersOneTable.isPlayersBoard != game.value!!.gamersOneTable.isPlayersBoard) {
                        if (downloadedGame.winner != null) {
                            end.postValue(true)
                        }
                        game.value = downloadedGame.apply {
                            gamersOneTable.isPlayersBoard = !gamersOneTable.isPlayersBoard
                            gamersTwoTable.isPlayersBoard = !gamersOneTable.isPlayersBoard
                        }
                    }
                }
            }
    }

    fun joinGame(id: String) {
        gameId.postValue(id)
        db.collection("testGames").document(id).get().addOnSuccessListener {
            val secondEmail = game.value!!.firstUserEmail
            val newGame = moshi.adapter(Game::class.java)
                .fromJson((it.data!!["data"].toString()))!!.apply {
                    gamersOneTable.isPlayersBoard = !gamersOneTable.isPlayersBoard
                    gamersTwoTable.isPlayersBoard = !gamersOneTable.isPlayersBoard
                }
            newGame.secondUserEmail = secondEmail
            game.value = newGame
            updateGame()
            db.collection("users").whereEqualTo("email", game.value!!.firstUserEmail).get()
                .addOnSuccessListener {
                    playerOneName.value = it.documents[0]["nickname"].toString()
                }
        }
        db.collection("testGames").document(id).addSnapshotListener { snapshot, e ->
            val downloadedGame = moshi.adapter(Game::class.java)
                .fromJson((snapshot!!.data!!["data"].toString()))!!
            if (secondPlayerName.value == null) {
                db.collection("users").whereEqualTo("email", downloadedGame.secondUserEmail)
                    .get().addOnSuccessListener {
                        if (!it.isEmpty)
                            secondPlayerName.value = it.documents[0]["nickname"].toString()
                    }
            }
            if (downloadedGame.gamersOneTable.isPlayersBoard != game.value!!.gamersOneTable.isPlayersBoard) {
                game.value = downloadedGame.apply {
                    gamersOneTable.isPlayersBoard = !gamersOneTable.isPlayersBoard
                    gamersTwoTable.isPlayersBoard = !gamersOneTable.isPlayersBoard
                }
            }
        }
    }

}
