package com.example.kotlinchatapp.UserRegister

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.kotlinchatapp.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    companion object{
        val TAG = "LOGIN"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
