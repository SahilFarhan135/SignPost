package com.example.networkdomain.network

sealed class NetworkResult<out T> {
    data class Success<out T>(val body: T, val responseCode: String) : NetworkResult<T>()
    data class Failure(val throwable: Throwable, val responseCode: String) :
        NetworkResult<Nothing>()

    fun result(): T {
        when (this) {
            is Success -> return body
            is Failure -> throw throwable
        }
    }

    fun resultIgnoreException(): T? {
        return when (this) {
            is Success -> body
            is Failure -> null
        }
    }
}