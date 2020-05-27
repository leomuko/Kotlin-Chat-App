package com.example.kotlinchatapp.UserRegister

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.kotlinchatapp.R
import com.example.kotlinchatapp.datamodels.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {
    companion object{
        val TAG = "SIGNUP"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        already_have_account_text.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        register_button.setOnClickListener {
            performUserRegistration()
        }

    }

    private fun performUserRegistration() {
        val email = register_email_input.text.toString()
        val password = register_password_input.text.toString()


        if (email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{
                if (it.isSuccessful){
                    Log.d(TAG, "Created new user")
                    saveUserToDatabase()
                }else{
                    return@addOnCompleteListener
                }
            }.addOnFailureListener {
                Log.d(TAG,"User registration not successful ${it.message}")
            }
    }

    private fun saveUserToDatabase() {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        Log.d(TAG,"$uid")
        val username = register_username_input.text.toString() ?: ""
        val databaseRef = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val userToSave = UserModel(
            uid,
            username
        )
        databaseRef.setValue(userToSave).addOnCompleteListener {
            Log.d(TAG,"User saved to database")
            val intent = Intent(this, UploadImageActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }.addOnFailureListener {

            Log.d(TAG,"User not saved to database")
        }

    }

    override fun onUserInteraction() {
        if (currentFocus != null){
            val inputMethodManager= ContextCompat.getSystemService(this, InputMethodManager::class.java)
            inputMethodManager?.hideSoftInputFromWindow(currentFocus?.windowToken,0)
        }

    }
}
