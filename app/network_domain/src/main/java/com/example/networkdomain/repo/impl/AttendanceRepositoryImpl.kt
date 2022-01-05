package com.example.networkdomain.repo.impl

import com.example.networkdomain.api.AttendanceApi
import com.example.networkdomain.model.AttendanceDto
import com.example.networkdomain.model.EmployeeDto
import com.example.networkdomain.repo.interaface.AttendanceRepository
import retrofit2.Response
import javax.inject.Inject

class AttendanceRepositoryImpl @Inject constructor(
    private val attendanceApi: AttendanceApi
) : AttendanceRepository {
    override suspend fun getAllEmployees(): Response<List<EmployeeDto>> {
        return attendanceApi.getAllEmployee()
    }

    override suspend fun getAttendance(requestAttendanceDto: Triple<String, String, String>): Response<List<AttendanceDto>> {
        return attendanceApi.getAttendance(
            requestAttendanceDto.first,
            requestAttendanceDto.second,
            requestAttendanceDto.third
        )
    }
}
