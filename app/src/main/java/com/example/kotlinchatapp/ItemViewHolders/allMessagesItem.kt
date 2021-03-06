package com.example.kotlinchatapp.ItemViewHolders

import com.example.kotlinchatapp.R
import com.example.kotlinchatapp.datamodels.ChatMessageModel
import com.example.kotlinchatapp.datamodels.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.all_messages_layout.view.*

class allMessagesItem(val chatMessageMessage: ChatMessageModel) :
    com.xwray.groupie.kotlinandroidextensions.Item() {
    var chatPartnerUser: UserModel? = null
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        when (chatMessageMessage.messageType) {
            "TEXT" -> viewHolder.itemView.user_latest_message.text = chatMessageMessage.text
            "IMAGE" -> viewHolder.itemView.user_latest_message.text = "Sent an Image"
            else ->{
                viewHolder.itemView.user_latest_message.text = chatMessageMessage.text
            }
        }
        var chatPatnerId: String = ""
        if (FirebaseAuth.getInstance().uid == chatMessageMessage.sendingUserId) {
            chatPatnerId = chatMessageMessage.receivingUserId
        } else {
            chatPatnerId = chatMessageMessage.sendingUserId
        }
        val dbref = FirebaseDatabase.getInstance().getReference("users/$chatPatnerId")
        dbref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                chatPartnerUser = p0.getValue(UserModel::class.java)
                viewHolder.itemView.username_text.text = chatPartnerUser?.username
                if (chatPartnerUser?.profileImageUrl != null) {
                    Picasso.get().load(chatPartnerUser?.profileImageUrl)
                        .into(viewHolder.itemView.user_circle_profileImage)
                    viewHolder.itemView.user_profileImage.alpha = 0f
                }
            }

        })
    }

    override fun getLayout(): Int {
        return R.layout.all_messages_layout
    }


}