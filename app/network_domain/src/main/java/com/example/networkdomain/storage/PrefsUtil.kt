package com.example.networkdomain.storage

import android.content.SharedPreferences

class PrefsUtil(
    private val pref: SharedPreferences
) {

    companion object {
        const val SHARED_PREFERENCE_ID = "SIGN_POST"
        private const val FCM_TOKEN = "FCM_TOKEN"
    }


    var fcmToken: String?
        get() = pref.getString(FCM_TOKEN, " ")
        set(value) = pref.edit()
            .putString(FCM_TOKEN, value)
            .apply()


}



