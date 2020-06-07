package com.example.kotlinchatapp.service

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    val SERVER_API = "AAAAKeeZ8Ro:APA91bHzH7JZf5vLRYEiZdDJj4xZbbigCI1g_4li3DFkXqzfpR1PPaCrBQP3G0j_i5yQm8fIHAkoNylhOKgN89sd7Nkcra4fZoa5ktsbcChdukrdxGVWr8oIKuwPYQrmqvtTRXD-MI89"
    val BASE_URL = "https://fcm.googleapis.com/"
    val getClient: ApiInterface
        get() {
            val gson = GsonBuilder()
                .setLenient()
                .create()
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

            return retrofit.create(ApiInterface::class.java)
        }


}