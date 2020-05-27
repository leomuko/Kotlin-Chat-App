package com.example.kotlinchatapp.datamodels

data class UserModel(val uid : String, val username: String) {
constructor() :this("","")
}