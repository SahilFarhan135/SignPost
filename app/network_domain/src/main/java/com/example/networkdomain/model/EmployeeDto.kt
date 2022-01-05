package com.example.networkdomain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EmployeeDto(
    val name: String? = null,
    val emp_id: String? = null
) : Parcelable
