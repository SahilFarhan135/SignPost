package com.example.signpost.firebase.service

import android.app.NotificationManager
import android.content.Context
import com.example.signpost.firebase.notification.NotificationBuilder
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class FirebaseCloudMessage : FirebaseMessagingService() {


    override fun onMessageReceived(p0: RemoteMessage) {
        createNotificationForChat(p0)
    }


    private fun createNotificationForChat(p0: RemoteMessage) {
        val notificationBuilder = NotificationBuilder(this)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        MainScope().launch {
            val notification = notificationBuilder.buildNotificationForChat(p0)
            notificationManager.notify("chatRequestResponse".hashCode(), notification)
        }
    }


    override fun onNewToken(token: String) {
    }


}