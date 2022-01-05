package com.example.networkdomain.usecase

import com.example.networkdomain.model.AttendanceDto
import com.example.networkdomain.network.NetworkResult
import com.example.networkdomain.network.safeApiCall
import com.example.networkdomain.repo.interaface.AttendanceRepository
import com.example.networkdomain.usecase.base.BaseSuspendUseCase
import javax.inject.Inject

class GetAttendanceUseCase @Inject constructor(
    private val repository: AttendanceRepository
) : BaseSuspendUseCase<NetworkResult<List<AttendanceDto>>, Triple<String, String, String>>() {
    override suspend fun perform(params: Triple<String, String, String>): NetworkResult<List<AttendanceDto>> {
        return when (val result = safeApiCall { repository.getAttendance(params) }) {
            is NetworkResult.Success -> NetworkResult.Success(result.body, result.responseCode)
            is NetworkResult.Failure -> result
        }
    }
}