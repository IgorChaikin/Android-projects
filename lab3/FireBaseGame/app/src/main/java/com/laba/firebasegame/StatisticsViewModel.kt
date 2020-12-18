package com.laba.firebasegame

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class StatisticsViewModel : ViewModel(){
    val users = MutableLiveData<List<User>>()
    var db = FirebaseFirestore.getInstance()

    fun loadUsers() {
        db.collection("users").get().addOnSuccessListener {
            val docs: List<DocumentSnapshot> = it.documents.sortedByDescending {
                it["won"] as Long
            }
            val users = mutableListOf<User>()
            for (doc in docs) {
                users.add(
                    User(
                        doc.data!!["nickname"] as String,
                        doc.data!!["email"] as String,
                        doc.data!!["gravatar"] as Boolean,
                        (doc.data!!["won"] as Long).toInt()
                    )
                )
            }
            this.users.value = users
        }
    }
}
