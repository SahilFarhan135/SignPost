package com.example.networkdomain.repo.impl

import com.example.networkdomain.model.FirebaseObj
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject


class FirebaseRepository @Inject constructor(
    private val database: DatabaseReference
) {
    fun sendDataToFirebase(
        obj: FirebaseObj
    ): Boolean {
        val reff = database.push().child("SignPost").setValue(obj)
        return true

    }

}