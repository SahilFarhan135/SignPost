package com.example.signpost.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.networkdomain.model.EmployeeDto
import com.example.networkdomain.network.Resources
import com.example.networkdomain.usecase.GetAllEmployeeUseCase
import com.example.signpost.base.ViewState
import com.example.signpost.util.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAllEmployeeUseCase: GetAllEmployeeUseCase
) : ViewModel() {

    private val _viewState = MutableLiveData<ViewState>(ViewState.Idle)
    val viewState = _viewState.toLiveData()


    private val _setEmployeeList = MutableLiveData<List<EmployeeDto>?>()
    val setEmployeeList = _setEmployeeList.toLiveData()

    private val _setError = MutableLiveData<String?>()
    val setError = _setError.toLiveData()


    init {
        //calling this method here gives error as the exceptionHandler is not instantitaed when this is called so its giving NPE
      //  getAllEmployee()
    }

    fun getAllEmployee() {
        _viewState.postValue(ViewState.Loading)
        getAllEmployeeUseCase().onEach { result ->
            when (result) {
                is Resources.Success -> {
                    _setEmployeeList.postValue(result.data)
                    _viewState.postValue(ViewState.Success())
                }
                is Resources.Error -> {
                    _setError.postValue(result.message)
                    _viewState.postValue(ViewState.Error())
                }
                is Resources.Loading -> {
                    _viewState.postValue(ViewState.Loading)
                }
            }
        }.launchIn(viewModelScope + exceptionHandler)
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        handleFailure(throwable = exception)
    }

    private fun handleFailure(throwable: Throwable?) {
        _viewState.postValue(ViewState.Error(throwable))
        Log.e("NETWORK_ERROR", throwable.toString())
    }


}

