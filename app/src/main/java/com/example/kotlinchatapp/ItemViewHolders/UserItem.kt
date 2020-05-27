package com.example.kotlinchatapp.ItemViewHolders

import com.example.kotlinchatapp.R
import com.example.kotlinchatapp.datamodels.UserModel
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.add_chat_item.view.*


class UserItem(val user: UserModel) : com.xwray.groupie.kotlinandroidextensions.Item() {
    override fun getLayout(): Int {
        return R.layout.add_chat_item
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.add_chat_username.text = user.username
        if (user.profileImageUrl != "") {
            Picasso.get().load(user.profileImageUrl)
                .into(viewHolder.itemView.add_chat_profile_image)
            viewHolder.itemView.imageView2.alpha = 0f
        }
    }
}