package com.example.signpost.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.networkdomain.model.AttendanceDto
import com.example.networkdomain.model.EmployeeDto
import com.example.networkdomain.model.RequestAttendanceDto
import com.example.networkdomain.network.Resources
import com.example.networkdomain.usecase.GetAllEmployeeUseCase
import com.example.networkdomain.usecase.GetAttendanceUseCase
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
    private val getAllEmployeeUseCase: GetAllEmployeeUseCase,
    private val getAttendanceUseCase: GetAttendanceUseCase
) : ViewModel() {

    private val _viewState = MutableLiveData<ViewState>(ViewState.Idle)
    val viewState = _viewState.toLiveData()


    private val _setEmployeeList = MutableLiveData<List<EmployeeDto>?>()
    val setEmployeeList = _setEmployeeList.toLiveData()

    private val _setError = MutableLiveData<String?>()
    val setError = _setError.toLiveData()


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


    private val _setAttendanceList = MutableLiveData<AttendanceDto?>()
    val setAttendanceList = _setAttendanceList.toLiveData()

    fun getAttendance(requestAttendanceDto: RequestAttendanceDto) {
        _viewState.postValue(ViewState.Loading)
        getAttendanceUseCase(requestAttendanceDto).onEach { result ->
            when (result) {
                is Resources.Success -> {
                    _setAttendanceList.postValue(result.data)
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

