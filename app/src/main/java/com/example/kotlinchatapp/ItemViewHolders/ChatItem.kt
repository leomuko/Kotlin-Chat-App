package com.example.kotlinchatapp.ItemViewHolders

import com.example.kotlinchatapp.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.activity_user_chat.view.*
import kotlinx.android.synthetic.main.receive_image_item.view.*
import kotlinx.android.synthetic.main.receivemessage_item.view.*
import kotlinx.android.synthetic.main.send_image_item.view.*
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

class SendImageItem(val text: String): com.xwray.groupie.kotlinandroidextensions.Item(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        Picasso.get().load(text).fit().centerCrop().into(viewHolder.itemView.sendmessage_image)
    }

    override fun getLayout(): Int {
        return R.layout.send_image_item
    }

}
class ReceiveImageItem(val text: String): com.xwray.groupie.kotlinandroidextensions.Item(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        Picasso.get().load(text).fit().centerCrop().into(viewHolder.itemView.receivemessage_image)
    }

    override fun getLayout(): Int {
        return R.layout.receive_image_item
    }

}