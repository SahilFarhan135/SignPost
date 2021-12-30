package com.example.signpost.firebase.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.signpost.R
import com.google.firebase.messaging.RemoteMessage


const val CHAT_NOTIFICATION_CHANNEL = "CHAT_NOTIFICATION_CHANNEL"

class NotificationBuilder(private val context: Context) {

    private val platformNotificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun buildNotificationForChat(
        data: RemoteMessage
    ): Notification {
        if (shouldCreateChatChannel())
            createChatChannel()
        val builder = NotificationCompat.Builder(context, CHAT_NOTIFICATION_CHANNEL)

        return builder
            .setContentTitle("Chat Message")
            .setContentText(data.notification?.tag)
            .setSmallIcon(R.drawable.ic_arrow_drop_down)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400))
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()
    }

    private fun shouldCreateChatChannel() =
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !chatChannelExists()

    @RequiresApi(Build.VERSION_CODES.O)
    private fun chatChannelExists() =
        platformNotificationManager.getNotificationChannel(CHAT_NOTIFICATION_CHANNEL) != null

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChatChannel() {

        val notificationChannel = NotificationChannel(
            CHAT_NOTIFICATION_CHANNEL, "Chat notification", NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Shows all the notification for chat"
        }
        platformNotificationManager.createNotificationChannel(notificationChannel)
    }

}