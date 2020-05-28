package com.example.kotlinchatapp.messages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.kotlinchatapp.ItemViewHolders.UserItem
import com.example.kotlinchatapp.R
import com.example.kotlinchatapp.datamodels.UserImageModel
import com.example.kotlinchatapp.datamodels.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_add_chats.*

class AddChats : AppCompatActivity() {
    companion object{
        val TAG = "ADD CHATS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_chats)
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Select Contacts"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        fetchUsers()

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun fetchUsers() {
        val dbref = FirebaseDatabase.getInstance().getReference("/users")

        dbref.addListenerForSingleValueEvent(object : ValueEventListener{
            val adapter = GroupAdapter<GroupieViewHolder>()
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    val user = it.getValue(UserModel::class.java)
                    if (user != null && user != AllMessages.currentlyLoggedInUser){

                        adapter.add(UserItem(user))
                    }
                    adapter.setOnItemClickListener { item, view ->
                        val userSelected = item as UserItem
                        val intent = Intent(view.context, UserChatActivity::class.java)
                        intent.putExtra("user",userSelected.user)
                        startActivity(intent)
                        finish()
                    }
                    add_chat_recycler_view.adapter = adapter
                }
            }

        })
    }


}
