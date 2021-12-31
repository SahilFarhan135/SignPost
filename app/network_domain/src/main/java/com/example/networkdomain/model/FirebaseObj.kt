package com.example.networkdomain.model

import java.io.Serializable

class FirebaseObj :Serializable{
    var mobileMake: String? = "",
    var mobileModel: String? = "",
    var androidVersion: String? = "",
    var appVersion: String? = "",
    var employeeName: String? = "",
    var fromDate: String? = "",
    var toDate: String? = "",
    var responseStatus: String? = "",
    var currentDateAndTime: String? = "",
    constructor() {}
    constructor(
        mobileMake: String,
        mobileModel: String,
        androidVersion: String,
        appVersion: String,
        employeeName: String,
        fromDate: String,
        toDate: String,
        responseStatus: Int,
        currentDateAndTime: String
    ) {
        this.mobileMake = mobileMake
        this.mobileModel = mobileModel
        this.androidVersion = androidVersion
        this.appVersion = appVersion
        this.employeeName = employeeName
        this.fromDate = fromDate
        this.toDate = toDate
        this.responseStatus = responseStatus
        this.currentDateAndTime = currentDateAndTime
    }
}

