package com.example.signpost.firebase.notification

import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build


class NotificationReceiver: BroadcastReceiver() {
    private var mMediaPlayer: MediaPlayer? = null

    override fun onReceive(context: Context, intent: Intent) {
        if(isAppIsInBackground(context)){
            playNotificationSound(context)
        }
    }

    private fun isAppIsInBackground(context: Context): Boolean {
        var isInBackground = true
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (am != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            val runningProcesses = am.runningAppProcesses
            try {
                for (processInfo in runningProcesses) {
                    if (processInfo.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        for (activeProcess in processInfo.pkgList) {
                            if (activeProcess == context.packageName) {
                                isInBackground = false
                            }
                        }
                    }
                }
            } catch (e: java.lang.Exception) {
            }
            return isInBackground
        }
        return isInBackground
    }

    private fun playNotificationSound(context: Context) {
        try {
            val alert = Uri.parse(
                "android.resource://"
                        + context.packageName + "/"  /*R.raw.notify*/
            )
            mMediaPlayer = MediaPlayer()
            mMediaPlayer!!.setDataSource(context, alert)
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            if (audioManager.getStreamVolume(AudioManager.STREAM_RING) != 0) {
                mMediaPlayer!!.setAudioStreamType(AudioManager.STREAM_RING)
                mMediaPlayer!!.prepare()
                mMediaPlayer!!.start()
            }
        } catch (e: Exception) {
        }
    }
}