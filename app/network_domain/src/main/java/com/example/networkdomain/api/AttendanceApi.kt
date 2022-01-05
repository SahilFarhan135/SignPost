package com.example.networkdomain.api

import com.example.networkdomain.model.AttendanceDto
import com.example.networkdomain.model.EmployeeDto
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface AttendanceApi {

    @GET(EndPoint.GET_ALL_EMPLOYEE)
    suspend fun getAllEmployee(): Response<List<EmployeeDto>>

    @FormUrlEncoded
    @POST(EndPoint.GET_ATTENDANCE)
    suspend fun getAttendance(
        @Field("emp_id") emp_id: String,
        @Field("from_dt") from_dt: String,
        @Field("to_dt") to_dt: String
    ): Response<List<AttendanceDto>>
}