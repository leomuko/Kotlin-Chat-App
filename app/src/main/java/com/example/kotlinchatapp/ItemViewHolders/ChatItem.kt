package com.example.kotlinchatapp.ItemViewHolders

import com.example.kotlinchatapp.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.activity_user_chat.view.*
import kotlinx.android.synthetic.main.receivemessage_item.view.*
import kotlinx.android.synthetic.main.sendmessage_item.view.*

class SendingItem(val text: String) : com.xwray.groupie.kotlinandroidextensions.Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.sendmessage_text.text = text
    }

    override fun getLayout(): Int {
        return R.layout.sendmessage_item
    }

}

class ReceivingItem(val text: String) : com.xwray.groupie.kotlinandroidextensions.Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.receivemessage_text.text = text
    }

    override fun getLayout(): Int {
        return R.layout.receivemessage_item
    }

}