package com.example.kotlinchatapp.datamodels

data class UserImageModel(val uid: String, val profileImageUrl: String) {
    constructor(): this("","")
}