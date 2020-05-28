package com.example.kotlinchatapp.datamodels

data class ChatModel(val id: String, val text: String, val sendingUserId:String, val receivingUserId: String, val timeStamp: Long) {
    constructor(): this("","","","",-1)
}