package com.example.kotlinchatapp.datamodels

data class UserModel(val uid : String, val username: String, var profileImageUrl: String) {
constructor() :this("","","")
}