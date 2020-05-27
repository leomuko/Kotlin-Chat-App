package com.example.kotlinchatapp.messages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.kotlinchatapp.R
import com.example.kotlinchatapp.UserRegister.LoginActivity
import com.example.kotlinchatapp.datamodels.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AllMessages : AppCompatActivity() {
    companion object{
        var currentlyLoggedInUser: UserModel? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_messages)
        fetchCurrentlyLoggedInUser()
    }

    private fun fetchCurrentlyLoggedInUser() {
        val uid = FirebaseAuth.getInstance().uid
        val dbref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        dbref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                currentlyLoggedInUser = p0.getValue(UserModel::class.java)
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.all_messages_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.menu_add_chats ->{
                val intent = Intent(this, AddChats::class.java)
                startActivity(intent)
            }
            R.id.menu_logout ->{
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }


}
