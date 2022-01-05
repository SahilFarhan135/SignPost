package com.example.signpost.ui.attendance

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.networkdomain.model.AttendanceDto
import com.example.networkdomain.model.EmployeeDto
import com.example.networkdomain.model.FirebaseObj
import com.example.networkdomain.network.NetworkResult
import com.example.networkdomain.repo.impl.FirebaseRepository
import com.example.networkdomain.usecase.GetAllEmployeeUseCase
import com.example.networkdomain.usecase.GetAttendanceUseCase
import com.example.signpost.base.ViewState
import com.example.signpost.util.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val getAttendanceUseCase: GetAttendanceUseCase,
    private val firebaseRepository: FirebaseRepository,
    private val getAllEmployeeUseCase: GetAllEmployeeUseCase
) : ViewModel() {


    private val _setEmployeeList = MutableLiveData<List<EmployeeDto>?>()
    val setEmployeeList = _setEmployeeList.toLiveData()

    private val _setResponseCodeForAttendanceList = MutableLiveData<String?>()
    val setResponseCodeForAttendanceList = _setResponseCodeForAttendanceList.toLiveData()


    private val _setAttendanceList = MutableLiveData<List<AttendanceDto>?>()
    val setAttendanceList = _setAttendanceList.toLiveData()

    fun saveDataToFireBase(obj: FirebaseObj) {
        _viewState.postValue(ViewState.Loading)
        val result = firebaseRepository.sendDataToFirebase(obj)
        _viewState.postValue(ViewState.Success())
    }


    fun getAttendancee(requestAttendanceDto: Triple<String, String, String>) {
        launch {
            _viewState.postValue(ViewState.Loading)
            when (val result = getAttendanceUseCase.perform(requestAttendanceDto)) {
                is NetworkResult.Success -> {
                    _setResponseCodeForAttendanceList.postValue(result.responseCode)
                    _setAttendanceList.postValue(result.body)
                    _viewState.postValue(ViewState.Success())
                }
                is NetworkResult.Failure -> {
                    _setResponseCodeForAttendanceList.postValue(result.responseCode)
                    throw result.throwable
                }
            }
        }
    }

    fun getAllEmployee() {
        launch {
            _viewState.postValue(ViewState.Loading)
            when (val result = getAllEmployeeUseCase.perform("")) {
                is NetworkResult.Success -> {
                    _setEmployeeList.postValue(result.body)
                    _viewState.postValue(ViewState.Success())
                }
                is NetworkResult.Failure -> throw result.throwable
            }
        }
    }

    private val _viewState = MutableLiveData<ViewState>(ViewState.Idle)
    val viewState = _viewState.toLiveData()


    private fun launch(
        code: suspend CoroutineScope.() -> Unit
    ) {
        (viewModelScope + exceptionHandler).launch {
            code.invoke(this)
        }
    }


    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        handleFailure(throwable = exception)
    }

    private fun handleFailure(throwable: Throwable?) {
        _viewState.postValue(ViewState.Error(throwable))
        Log.e("NETWORK_ERROR", throwable.toString())
    }
}

