package com.vishalsingh444888.sweatsync.di

import com.vishalsingh444888.sweatsync.data.api.ExerciseApi
import com.vishalsingh444888.sweatsync.repository.NetworkRepository
import com.vishalsingh444888.sweatsync.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideExerciseApiService():ExerciseApi{
        val headerInterceptor = Interceptor{chain ->
            val request = chain.request().newBuilder()
                .addHeader("X-RapidAPI-Key", "xxxxx")
                .addHeader("X-RapidAPI-Host", "xxxx")
                .build()
            chain.proceed(request)
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://exercisedb.p.rapidapi.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ExerciseApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(api: ExerciseApi):Repository{
        return NetworkRepository(api)
    }
}