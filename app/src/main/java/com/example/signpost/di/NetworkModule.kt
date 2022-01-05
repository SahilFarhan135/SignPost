package com.example.signpost.di

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import com.example.networkdomain.api.AttendanceApi
import com.example.networkdomain.network.NetworkClient
import com.example.networkdomain.network.NetworkManager
import com.example.networkdomain.repo.impl.AttendanceRepositoryImpl
import com.example.networkdomain.repo.impl.FirebaseRepository
import com.example.networkdomain.repo.interaface.AttendanceRepository
import com.example.networkdomain.storage.PrefsUtil
import com.example.networkdomain.usecase.GetAllEmployeeUseCase
import com.example.networkdomain.usecase.GetAttendanceUseCase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): AttendanceApi {
        return retrofit.create(AttendanceApi::class.java)
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(
        context: Context
    ): SharedPreferences {
        return context.getSharedPreferences(
            PrefsUtil.SHARED_PREFERENCE_ID, Context.MODE_PRIVATE
        )
    }

    @Provides
    @Singleton
    fun providePrefUtils(sharedPreferences: SharedPreferences): PrefsUtil {
        return PrefsUtil(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideAttendanceRepository(api: AttendanceApi): AttendanceRepository {
        return AttendanceRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideGetAttendanceUseCase(repo: AttendanceRepository): GetAttendanceUseCase {
        return GetAttendanceUseCase(repo)
    }

    @Provides
    @Singleton
    fun provideGetAllEmployeeUseCase(repo: AttendanceRepository): GetAllEmployeeUseCase {
        return GetAllEmployeeUseCase(repo)
    }

    @Singleton
    @Provides
    fun provideNetworkManager(
        @ApplicationContext context: Context
    ): NetworkManager {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return NetworkManager(connectivityManager)
    }


    @Singleton
    @Provides
    fun provideOkHttpClient(
        networkManager: NetworkManager
    ): OkHttpClient {
        return NetworkClient.provideOkHttp(
            networkManager
        )
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit = NetworkClient.provideRetrofit(okHttpClient)

    @Provides
    @Singleton
    fun provideFirebase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    @Provides
    @Singleton
    fun provideDatabase(firebaseDatabase: FirebaseDatabase): DatabaseReference {
        val dtr = firebaseDatabase.getReference("Main")
        return dtr
    }

    @Provides
    @Singleton
    fun provideFireBaseRepository(databaseReference: DatabaseReference): FirebaseRepository {
        return FirebaseRepository(databaseReference)
    }

}
