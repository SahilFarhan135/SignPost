package com.example.signpost.di

import android.content.Context
import android.net.ConnectivityManager
import com.example.networkdomain.api.AttendanceApi
import com.example.networkdomain.network.NetworkClient
import com.example.networkdomain.network.NetworkManager
import com.example.networkdomain.repository.truckrepository.AttendanceRepository
import com.example.networkdomain.repository.truckrepository.AttendanceRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton
import com.google.firebase.database.DatabaseReference

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): AttendanceApi {
        return retrofit.create(AttendanceApi::class.java)
    }


    @Provides
    @Singleton
    fun provideCoinRepository(api: AttendanceApi): AttendanceRepository {
        return AttendanceRepositoryImpl(api)
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
        val dtr=firebaseDatabase.getReference("Main")
        return dtr
    }

}
