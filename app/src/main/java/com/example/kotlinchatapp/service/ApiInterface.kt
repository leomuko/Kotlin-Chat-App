package com.example.kotlinchatapp.service

import com.example.kotlinchatapp.datamodels.RootModel

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiInterface {

    @Headers(
        "Authorization:key=AAAAKeeZ8Ro:APA91bHzH7JZf5vLRYEiZdDJj4xZbbigCI1g_4li3DFkXqzfpR1PPaCrBQP3G0j_i5yQm8fIHAkoNylhOKgN89sd7Nkcra4fZoa5ktsbcChdukrdxGVWr8oIKuwPYQrmqvtTRXD-MI89",
        "Content-Type:application/json"
    )
    @POST("fcm/send")
    fun postNotification(@Body root: RootModel): Call<ResponseBody>

}