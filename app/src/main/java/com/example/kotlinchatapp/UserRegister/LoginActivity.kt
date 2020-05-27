package com.example.kotlinchatapp.UserRegister

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.kotlinchatapp.R
import com.example.kotlinchatapp.messages.AllMessages
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    companion object{
        val TAG = "LOGIN"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null){
            Log.d(TAG,"User ${user}logged in")
            val intent = Intent(this, AllMessages::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }else{
            Log.d(TAG,"User needs to be logged in")
        }
        setContentView(R.layout.activity_login)


        create_new_account_text.setOnClickListener {
            val intent = Intent(this,SignupActivity::class.java)
            startActivity(intent)
        }
        login_button.setOnClickListener {
            performUserLogin()
        }


    }

    private fun performUserLogin() {
        val email: String = login_username_input.text.toString()
        val password: String = login_password_input.text.toString()

        if (email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener {
            if (it.isSuccessful){
                Toast.makeText(this,"User successfully logged in", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, AllMessages::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

            }else{
                return@addOnCompleteListener
            }
        }.addOnFailureListener {
            Toast.makeText(this,"Please enter valide user email and password", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onUserInteraction() {
        if (currentFocus != null){
            val inputMethodManager= ContextCompat.getSystemService(this, InputMethodManager::class.java)
            inputMethodManager?.hideSoftInputFromWindow(currentFocus?.windowToken,0)
        }

    }
}
