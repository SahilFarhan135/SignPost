package com.example.networkdomain.repository.truckrepository

import com.example.networkdomain.model.AttendanceDto
import com.example.networkdomain.model.EmployeeDto
import com.example.networkdomain.model.RequestAttendanceDto

interface AttendanceRepository {
    suspend fun getAllEmployees(): List<EmployeeDto>
    suspend fun getAttendance(requestAttendanceDto: RequestAttendanceDto): AttendanceDto
}