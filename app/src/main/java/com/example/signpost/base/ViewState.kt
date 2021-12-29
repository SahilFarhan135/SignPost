package com.example.signpost.base

sealed class ViewState {
    object Idle : ViewState()
    object Loading : ViewState()
    data class Success(val message: String? = null) : ViewState()
    data class Error(val throwable: Throwable? = null) : ViewState()
}