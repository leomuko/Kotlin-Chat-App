package com.example.kotlinchatapp.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Build.VERSION_CODES
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.kotlinchatapp.R
import com.example.kotlinchatapp.messages.UserChatActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseInstanceIdService : FirebaseMessagingService() {
    companion object {
        val TAG = "MESSAGING_INSTANCE"
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        Log.d(TAG, "Message data payload: " + p0.from)
        var msg = ""
        if (p0.data.isNotEmpty()) {
            msg = p0.data.get("message").toString()
            val msgType = p0.data.get("messagType").toString()
            val sendingUser = p0.data.get("sendingUserName").toString()
            if (msgType == "TEXT"){
                sendNotification(msg,sendingUser)
            }else{
                msg = "Sent an Image"
                sendNotification(msg, sendingUser)
            }

        }
    }

    private fun sendNotification(
        messageBody: String,
        sendingUserName: String
    ) {
        val intent = Intent(this, UserChatActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val channelId = "00000"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(sendingUserName)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0,notificationBuilder.build())
    }


}
