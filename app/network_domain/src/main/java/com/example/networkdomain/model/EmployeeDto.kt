package com.example.networkdomain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EmployeeDto(
	val name: String? = null,
	val empId: String? = null
) : Parcelable
