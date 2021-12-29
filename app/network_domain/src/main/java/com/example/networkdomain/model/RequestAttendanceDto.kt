package com.example.networkdomain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RequestAttendanceDto(
    var emp_id: Int? = null,
    var from_dt: String? = null,
    var to_dt: String? = null,
   
):Parcelable
/*
* emp_id=1603
from_dt=2019-08-01
to_dt=2019-08-31*/
