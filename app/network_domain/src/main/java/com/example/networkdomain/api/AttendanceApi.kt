package com.example.networkdomain.api

import com.example.networkdomain.model.AttendanceDto
import com.example.networkdomain.model.EmployeeDto
import com.example.networkdomain.model.RequestAttendanceDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AttendanceApi {

    @GET(EndPoint.GET_ALL_EMPLOYEE)
    suspend fun getAllEmployee(): List<EmployeeDto>

    @POST(EndPoint.GET_ATTENDANCE)
    suspend fun getAttendance(@Body request: RequestAttendanceDto): AttendanceDto
}