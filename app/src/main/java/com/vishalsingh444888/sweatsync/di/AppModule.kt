package com.vishalsingh444888.sweatsync.di

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.vishalsingh444888.sweatsync.PreferenceManager
import com.vishalsingh444888.sweatsync.R
import com.vishalsingh444888.sweatsync.data.api.ExerciseApi
import com.vishalsingh444888.sweatsync.repository.AuthRepository
import com.vishalsingh444888.sweatsync.repository.AuthRepositoryImpl
import com.vishalsingh444888.sweatsync.repository.NetworkRepository
import com.vishalsingh444888.sweatsync.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
                .addHeader("X-RapidAPI-Key", "xxxx")
                .addHeader("X-RapidAPI-Host", "exercisedb.p.rapidapi.com")
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
    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepositoryImpl(firebaseAuth: FirebaseAuth): AuthRepository{
        return AuthRepositoryImpl(firebaseAuth)
    }
    @Provides
    @Singleton
    fun provideGoogleSigInOption(@ApplicationContext context: Context): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestProfile()
            .requestIdToken(context.getString(R.string.web_client_id))
            .build()
        return GoogleSignIn.getClient(context,gso)
    }
    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): PreferenceManager{
        return PreferenceManager(context = context)
    }
}