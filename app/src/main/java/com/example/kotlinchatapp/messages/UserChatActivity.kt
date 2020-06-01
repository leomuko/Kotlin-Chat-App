package com.example.kotlinchatapp.messages

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotlinchatapp.ItemViewHolders.ReceiveImageItem
import com.example.kotlinchatapp.ItemViewHolders.ReceivingItem
import com.example.kotlinchatapp.ItemViewHolders.SendImageItem
import com.example.kotlinchatapp.ItemViewHolders.SendingItem
import com.example.kotlinchatapp.R
import com.example.kotlinchatapp.datamodels.ChatMessageModel
import com.example.kotlinchatapp.datamodels.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_user_chat.*
import java.util.*

class UserChatActivity : AppCompatActivity() {
    companion object{
        val SEND_IMAGE_CODE = 0
    }

    var selectedUser: UserModel? = null
    val adapter = GroupAdapter<GroupieViewHolder>()
    var imageToSendUrl: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_chat)

        selectedUser = intent.getParcelableExtra("user")
        supportActionBar?.title = selectedUser!!.username
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        user_chat_recyclerview.adapter = adapter
        listenForMessages()
        fab_send_image.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, SEND_IMAGE_CODE)
        }
        send_message_button.setOnClickListener {
            performSendMessage()
        }

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SEND_IMAGE_CODE && resultCode == Activity.RESULT_OK && data != null){
            imageToSendUrl = data.data
            saveImageMessageToDatabase()
        }
    }

    private fun saveImageMessageToDatabase() {
        val fileName = UUID.randomUUID().toString()
        val storageRef = FirebaseStorage.getInstance().getReference("/image_messages/$fileName")
        storageRef.putFile(imageToSendUrl!!).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener {
                performSendImageMessage(it.toString())
            }
        }

    }

    private fun performSendImageMessage(imageToSend: String) {

        val sendingUserId = FirebaseAuth.getInstance().uid
        val receivingUserId = selectedUser?.uid

        val sendingRef = FirebaseDatabase.getInstance()
            .getReference("/user-messages/$sendingUserId/$receivingUserId").push()
        val receivingRef = FirebaseDatabase.getInstance()
            .getReference("/user-messages/$receivingUserId/$sendingUserId").push()

        if (sendingUserId == null) return
        val chatMessage = ChatMessageModel(
            sendingRef.key!!,
            "IMAGE",
            imageToSend,
            sendingUserId,
            receivingUserId!!,
            System.currentTimeMillis() / 1000

        )
        sendingRef.setValue(chatMessage).addOnSuccessListener {
            user_chat_message_entry.text?.clear()
            user_chat_recyclerview.scrollToPosition(adapter.itemCount - 1)
        }

        receivingRef.setValue(chatMessage)
        val latestMessagesSentRef = FirebaseDatabase.getInstance()
            .getReference("/latest-messages/$sendingUserId/$receivingUserId")
        latestMessagesSentRef.setValue(chatMessage)
        val latestMessagesReceivedRef = FirebaseDatabase.getInstance()
            .getReference("/latest-messages/$receivingUserId/$sendingUserId")
        latestMessagesReceivedRef.setValue(chatMessage)
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
        val chatMessage = ChatMessageModel(
            sendingRef.key!!,
            "TEXT",
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
        val latestMessagesSentRef = FirebaseDatabase.getInstance()
            .getReference("/latest-messages/$sendingUserId/$receivingUserId")
        latestMessagesSentRef.setValue(chatMessage)
        val latestMessagesReceivedRef = FirebaseDatabase.getInstance()
            .getReference("/latest-messages/$receivingUserId/$sendingUserId")
        latestMessagesReceivedRef.setValue(chatMessage)


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

                val chatAdded = p0.getValue(ChatMessageModel::class.java)
                if (chatAdded != null) {
                    if (chatAdded.sendingUserId == sendingUserId) {
                        when (chatAdded.messageType){
                            "TEXT" -> adapter.add(SendingItem(chatAdded.text))
                            "IMAGE" -> adapter.add(SendImageItem(chatAdded.text))
                            else ->{
                                adapter.add(SendingItem(chatAdded.text))
                            }
                        }
                    } else {
                        when(chatAdded.messageType) {
                           "TEXT" -> adapter.add(ReceivingItem(chatAdded.text))
                            "IMAGE" -> adapter.add(ReceiveImageItem(chatAdded.text))
                            else ->{
                                adapter.add(ReceivingItem(chatAdded.text))
                            }
                        }
                    }


                }

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

        })

    }


}
