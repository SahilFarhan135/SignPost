package com.example.networkdomain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AttendanceDto(
    val entry_at: String? = null,
    val exit_at: String? = null,
    val emp_Id: String? = null
) : Parcelable
