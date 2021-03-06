package com.example.networkdomain.network


sealed class Resources<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resources<T>(data)
    class Error<T>(message: String, data: T? = null) : Resources<T>(data, message)
    class Loading<T>(data: T? = null) : Resources<T>(data)
    class StatusCode<T>(responseCode: String, dataa: T? = null) : Resources<T>(dataa, responseCode)
}