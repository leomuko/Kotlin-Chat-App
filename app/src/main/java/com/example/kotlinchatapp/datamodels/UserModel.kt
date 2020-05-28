package com.example.kotlinchatapp.datamodels

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserModel(val uid : String, val username: String, var profileImageUrl: String):Parcelable {
constructor() :this("","","")
}