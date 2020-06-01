package com.example.kotlinchatapp.datamodels

data class ChatMessageModel(val id: String, val messageType: String, val text: String, val sendingUserId:String, val receivingUserId: String, val timeStamp: Long) {
    constructor(): this("","","","","",-1)
}