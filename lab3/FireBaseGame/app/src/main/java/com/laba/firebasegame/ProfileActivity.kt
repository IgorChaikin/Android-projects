package com.laba.firebasegame

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.firebase.storage.FirebaseStorage
import com.hazelcast.util.MD5Util
import com.laba.firebasegame.databinding.ActivityProfileBinding
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream


class ProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfileBinding
    val viewModel: EditAccountViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        viewModel.user.observe(this, Observer{
            binding.nickname.setText(it.nickname)
            binding.gravatarSwitch.isChecked = it.isGravatar
            if(it.isGravatar) {
                val hash: String = MD5Util.toMD5String(it.email)
                val gravatarUrl =
                    "http://www.gravatar.com/avatar/$hash?s=80004&d=mp"
                Picasso.get()
                    .load(gravatarUrl)
                    .placeholder(R.drawable.ic_baseline_account_circle_24)
                    .into(binding.avatar)
            } else {
                val ref = FirebaseStorage.getInstance().reference.child("images/" + it.nickname + ".png").downloadUrl.addOnSuccessListener {
                    Picasso.get()
                        .load(it)
                        .placeholder(R.drawable.ic_baseline_account_circle_24)
                        .into(binding.avatar)
                }
            }
        })
        binding.submit.setOnClickListener {
            viewModel.changeNickName(binding.nickname.text.toString())
        }
        binding.gravatarSwitch.setOnCheckedChangeListener { compoundButton, b ->
            if(b){
                viewModel.changeGravater(b)
                val hash: String = MD5Util.toMD5String(viewModel.user.value!!.email)
                val gravatarUrl =
                    "http://www.gravatar.com/avatar/$hash?s=80004&d=mp"
                Picasso.get()
                    .load(gravatarUrl)
                    .placeholder(R.drawable.ic_baseline_account_circle_24)
                    .into(binding.avatar)
            } else{
                val getImageIntent = Intent(Intent.ACTION_GET_CONTENT)
                getImageIntent.type = "image/*"
                startActivityForResult(getImageIntent, 14)
            }
        }
        viewModel.getUser(intent.getStringExtra("email")!!)
        setContentView(binding.root)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 14 && resultCode == Activity.RESULT_OK){
            val selectedImageUri: Uri = data?.data!!
            val instr = contentResolver.openInputStream(selectedImageUri)
            var bitmap = BitmapFactory.decodeStream(instr)
            val outStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, outStream)
            bitmap = BitmapFactory.decodeByteArray(outStream.toByteArray(), 0, outStream.toByteArray().size)
            binding.avatar.setImageBitmap(bitmap)
            val ref = FirebaseStorage.getInstance().reference.child("images/" + viewModel.user.value!!.nickname + ".png")
            ref.putFile(selectedImageUri)
            viewModel.changeGravater(false)
        }
    }
}
