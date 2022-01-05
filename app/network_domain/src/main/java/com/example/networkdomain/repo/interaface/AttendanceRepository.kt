package com.example.networkdomain.repo.interaface

import com.example.networkdomain.model.AttendanceDto
import com.example.networkdomain.model.EmployeeDto
import retrofit2.Response

interface AttendanceRepository {
    suspend fun getAllEmployees(): Response<List<EmployeeDto>>
    suspend fun getAttendance(requestAttendanceDto: Triple<String, String, String>): Response<List<AttendanceDto>>
}