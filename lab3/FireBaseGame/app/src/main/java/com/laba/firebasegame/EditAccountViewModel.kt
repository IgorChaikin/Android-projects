package com.laba.firebasegame

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream


class EditAccountViewModel : ViewModel() {
    val user = MutableLiveData<User>()
    var db = FirebaseFirestore.getInstance()

    fun changeNickName(nickname: String){
        db.collection("users").whereEqualTo("email", user.value!!.email).get().addOnSuccessListener {
            val docRef = it.documents[0].reference
            docRef.update("nickname", nickname)
        }
    }

    fun changeGravater(isGravatar: Boolean){
        db.collection("users").whereEqualTo("email", user.value!!.email).get().addOnSuccessListener {
            val docRef = it.documents[0].reference
            docRef.update("gravatar", isGravatar)
        }
    }

    fun getUser(email: String) {
        db.collection("users").whereEqualTo("email", email).get().addOnSuccessListener {
            if (it.documents.isNotEmpty()) {
                val doc = it.documents[0]
                user.value = User(
                    doc.data!!["nickname"] as String,
                    doc.data!!["email"] as String,
                    doc.data!!["gravatar"] as Boolean,
                    (doc.data!!["won"] as Long).toInt()
                )
            }
        }
    }
}
