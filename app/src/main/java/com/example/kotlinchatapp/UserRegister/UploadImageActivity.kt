package com.example.kotlinchatapp.UserRegister

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import com.example.kotlinchatapp.R
import com.example.kotlinchatapp.datamodels.UserImageModel
import com.example.kotlinchatapp.messages.AllMessages
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_upload_image.*
import java.util.*

class UploadImageActivity : AppCompatActivity() {
    companion object {
        val IMAGE_SELECTION_CODE = 0
        val TAG = "IMAGE UPLOAD"
    }

    var selectedPhotoUrl: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_image)

        profile_image.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_SELECTION_CODE)
        }
        upload_image_button.setOnClickListener {
            saveImageToDatabase()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_SELECTION_CODE && resultCode == Activity.RESULT_OK && data != null) {
            selectedPhotoUrl = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUrl)
            selected_image.setImageBitmap(bitmap)
            profile_image.alpha = 0f
        }
    }

    private fun saveImageToDatabase() {
        val fileName = UUID.randomUUID().toString()

        val storageRef = FirebaseStorage.getInstance().getReference("/profileImages/$fileName")


        storageRef.putFile(selectedPhotoUrl!!).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener {
                //profileImageUrl = it.toString()
                saveUserProfileImage(it.toString())
            }


        }.addOnFailureListener {
            Log.d(TAG, "Image not uploaded")
        }

    }

    private fun saveUserProfileImage(profileImageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        //val dbref = FirebaseDatabase.getInstance().getReference("/user-images/$uid")
        val dbref = FirebaseDatabase.getInstance().getReference("/users").child("$uid").child("profileImageUrl")
//        var imageToStore: UserImageModel = UserImageModel(
//            uid,
//            profileImageUrl!!
//        )
        dbref.setValue(profileImageUrl).addOnSuccessListener {
            Log.d(TAG, "user profile image successfully stored")
            val intent = Intent(this, AllMessages::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }.addOnFailureListener {
            Log.d(TAG, "user Image not successfully stored")
        }
    }


}

