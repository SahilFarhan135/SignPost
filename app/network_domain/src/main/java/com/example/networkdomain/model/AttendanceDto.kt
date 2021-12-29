package com.example.networkdomain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AttendanceDto(
	val entryAt: String? = null,
	val exitAt: String? = null,
	val empId: String? = null
) : Parcelable
