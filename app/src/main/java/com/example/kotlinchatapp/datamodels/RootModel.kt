package com.example.kotlinchatapp.datamodels

import com.google.gson.annotations.SerializedName

data class RootModel (
    @SerializedName("to")
    val token: String,
    @SerializedName("data")
    val sentMessage : DataModel
)