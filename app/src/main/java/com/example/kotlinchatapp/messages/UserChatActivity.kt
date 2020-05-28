package com.example.kotlinchatapp.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.kotlinchatapp.ItemViewHolders.ReceivingItem
import com.example.kotlinchatapp.ItemViewHolders.SendingItem
import com.example.kotlinchatapp.R
import com.example.kotlinchatapp.datamodels.ChatModel
import com.example.kotlinchatapp.datamodels.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_user_chat.*

class UserChatActivity : AppCompatActivity() {

    var selectedUser: UserModel? = null
    val adapter = GroupAdapter<GroupieViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_chat)

        selectedUser = intent.getParcelableExtra("user")
        supportActionBar?.title = selectedUser!!.username
        actionBar?.setDisplayHomeAsUpEnabled(true)

        user_chat_recyclerview.adapter = adapter
        listenForMessages()
        send_message_button.setOnClickListener {
            performSendMessage()
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun performSendMessage() {
        val message = user_chat_message_entry.text.toString()
        val sendingUserId = FirebaseAuth.getInstance().uid
        val receivingUserId = selectedUser?.uid

        val sendingRef = FirebaseDatabase.getInstance()
            .getReference("/user-messages/$sendingUserId/$receivingUserId").push()
        val receivingRef = FirebaseDatabase.getInstance()
            .getReference("/user-messages/$receivingUserId/$sendingUserId").push()

        if (sendingUserId == null) return
        val chatMessage = ChatModel(
            sendingRef.key!!,
            message,
            sendingUserId,
            receivingUserId!!,
            System.currentTimeMillis() / 1000
        )
        sendingRef.setValue(chatMessage).addOnSuccessListener {
            user_chat_message_entry.text?.clear()
            user_chat_recyclerview.scrollToPosition(adapter.itemCount - 1)
        }

        receivingRef.setValue(chatMessage)
        val allMessagesSentRef = FirebaseDatabase.getInstance()
            .getReference("/all-messages/$sendingUserId/$receivingUserId").push()
        allMessagesSentRef.setValue(chatMessage)
        val allMessagesReceivedRef = FirebaseDatabase.getInstance()
            .getReference("/user-messages/$receivingUserId/$sendingUserId").push()
        allMessagesReceivedRef.setValue(chatMessage)


    }

    private fun listenForMessages() {
        val sendingUserId = FirebaseAuth.getInstance().uid
        val receivingUserId = selectedUser?.uid
        val dbref = FirebaseDatabase.getInstance()
            .getReference("/user-messages/$sendingUserId/$receivingUserId")
        dbref.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {

                val chatAdded = p0.getValue(ChatModel::class.java)
                if (chatAdded != null) {
                    Log.d("UserChat","${chatAdded.text}")
                    if (chatAdded.sendingUserId == sendingUserId) {

                        adapter.add(SendingItem(chatAdded.text))
                    } else {
                        adapter.add(ReceivingItem(chatAdded.text))

                    }
                }


            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

        })

    }
}
