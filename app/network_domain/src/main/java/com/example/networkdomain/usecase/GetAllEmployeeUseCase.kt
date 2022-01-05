package com.example.networkdomain.usecase

import com.example.networkdomain.model.EmployeeDto
import com.example.networkdomain.network.NetworkResult
import com.example.networkdomain.network.safeApiCall
import com.example.networkdomain.repo.interaface.AttendanceRepository
import com.example.networkdomain.usecase.base.BaseSuspendUseCase
import javax.inject.Inject

class GetAllEmployeeUseCase @Inject constructor(
    private val repository: AttendanceRepository
) : BaseSuspendUseCase<NetworkResult<List<EmployeeDto>>, String>() {
    override suspend fun perform(params: String): NetworkResult<List<EmployeeDto>> {
        return when (val result = safeApiCall { repository.getAllEmployees() }) {
            is NetworkResult.Success -> NetworkResult.Success(result.body, result.responseCode)
            is NetworkResult.Failure -> result
        }
    }
}