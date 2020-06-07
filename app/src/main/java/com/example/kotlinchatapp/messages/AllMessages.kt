package com.example.kotlinchatapp.messages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.kotlinchatapp.ItemViewHolders.allMessagesItem
import com.example.kotlinchatapp.R
import com.example.kotlinchatapp.UserRegister.LoginActivity
import com.example.kotlinchatapp.datamodels.ChatMessageModel
import com.example.kotlinchatapp.datamodels.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_all_messages.*

class AllMessages : AppCompatActivity() {
    companion object{
        var currentlyLoggedInUser: UserModel? = null
        val TAG = "ALL MESSAGES"
    }

    val latestMessagesMap = HashMap<String, ChatMessageModel>()
    val adapter = GroupAdapter<GroupieViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_messages)

        all_messages_recyclerview.adapter = adapter
        all_messages_recyclerview.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))


        fetchCurrentlyLoggedInUser()
        loadLatestMessages()

    }

    private fun refreshRecyclerView(){
        adapter.clear()
        latestMessagesMap.values.forEach{
            adapter.add(allMessagesItem(it))
        }
    }

    private fun loadLatestMessages() {
        val sendingUserId = FirebaseAuth.getInstance().uid
        val dbref = FirebaseDatabase.getInstance().getReference("/latest-messages/$sendingUserId")
        dbref.addChildEventListener(object :ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val chatMessageMessage: ChatMessageModel = p0.getValue(ChatMessageModel::class.java) ?: return

                latestMessagesMap[p0.key!!] = chatMessageMessage
                refreshRecyclerView()
                adapter.setOnItemClickListener { item, view ->
                    val userSelected = item as allMessagesItem
                    val userToChat = userSelected.chatPartnerUser
                    val intent = Intent(view.context, UserChatActivity::class.java)
                    intent.putExtra("user",userToChat)
                    startActivity(intent)
                }

            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessageMessage: ChatMessageModel = p0.getValue(ChatMessageModel::class.java) ?: return
                adapter.add(allMessagesItem(chatMessageMessage))
                latestMessagesMap[p0.key!!] = chatMessageMessage
                refreshRecyclerView()
                adapter.setOnItemClickListener { item, view ->
                    val userSelected = item as allMessagesItem
                    val userToChat = userSelected.chatPartnerUser
                    val intent = Intent(view.context, UserChatActivity::class.java)
                    intent.putExtra("user",userToChat)
                    startActivity(intent)
                }

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

        })
    }

    private fun fetchCurrentlyLoggedInUser() {
        val uid = FirebaseAuth.getInstance().uid
        val dbref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        dbref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                getAndSetUserToken()
                currentlyLoggedInUser = p0.getValue(UserModel::class.java)
            }

        })
    }

    private fun getAndSetUserToken() {
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
            if (it.isSuccessful){
                val token  = it.result!!.token
                saveToken(token)
            }
        }
    }

    private fun saveToken(token: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val dbref = FirebaseDatabase.getInstance().getReference("/users").child("$uid").child("userToken")
        dbref.setValue(token).addOnSuccessListener {
            Log.d(TAG, "User with uid ${uid} has token ${token}")
        }
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
