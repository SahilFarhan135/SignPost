package com.example.networkdomain.repository.repo

import com.google.firebase.database.DatabaseReference
import javax.inject.Inject


class FirebaseRepository @Inject constructor(
    private val database: DatabaseReference
) {
    fun sendDataToFirebase(
        obj: FirebaseObj
    ): Boolean {
        val reff = database.child("SignPost").setValue(obj)
        return true
    }

}