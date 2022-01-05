package com.example.networkdomain.network


import com.example.networkdomain.network.NetworkResult.Failure
import okhttp3.ResponseBody
import retrofit2.Response

suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): NetworkResult<T> {
    return io {
        try {
            apiCall.invoke()
                .resolveResponse()
        } catch (e: Exception) {
            Failure(e, apiCall.invoke().code().toString())
        }
    }
}

private fun <T> Response<T>.resolveResponse(): NetworkResult<T> {
    return if (this.isSuccessful) {
        this.body()
            ?.let {
                NetworkResult.Success(it, code().toString())
            } ?: kotlin.run {
            Failure(
                this.errorBody()
                    .toApiError(),
                this.code().toString()
            )
        }
    } else {
        handleFailure()
    }
}


private fun <T> Response<T>.handleFailure(): Failure {
    if (code() == 403) return Failure(Exception("Unauthorized Exception"), code().toString())
    return Failure(
        this.errorBody()
            .toApiError(), code().toString()
    )
}

private fun ResponseBody?.toApiError(): Exception {
    return Exception("API Error")
}