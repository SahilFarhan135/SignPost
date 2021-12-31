package com.example.networkdomain.repository.truckrepository

import com.example.networkdomain.api.AttendanceApi
import com.example.networkdomain.model.AttendanceDto
import com.example.networkdomain.model.EmployeeDto
import com.example.networkdomain.model.RequestAttendanceDto
import javax.inject.Inject

class AttendanceRepositoryImpl @Inject constructor(
    private val attendanceApi: AttendanceApi
) : AttendanceRepository {
    override suspend fun getAllEmployees(): List<EmployeeDto> = attendanceApi.getAllEmployee()
    override suspend fun getAttendance(requestAttendanceDto: RequestAttendanceDto): AttendanceDto {
        return attendanceApi.getAttendance(requestAttendanceDto)
    }
}