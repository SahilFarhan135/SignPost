package com.example.networkdomain.usecase

import android.util.Log
import com.example.networkdomain.model.EmployeeDto
import com.example.networkdomain.network.Resources
import com.example.networkdomain.repository.truckrepository.AttendanceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetAllEmployeeUseCase @Inject constructor(
    private val repository: AttendanceRepository
) {
    operator fun invoke(): Flow<Resources<List<EmployeeDto>>> = flow {
        try {
            emit(Resources.Loading())
            val employeeListResult = repository.getAllEmployees()
            emit(Resources.Success(employeeListResult))
        } catch (e: HttpException) {
            emit(Resources.Error(e.localizedMessage ?: "Something went wrong"))
            Log.d("OK", e.localizedMessage.plus(e.code()))
        } catch (e: IOException) {
            emit(Resources.Error("Couldn't connect to server"))
        }
    }
}