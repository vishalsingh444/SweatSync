package com.vishalsingh444888.sweatsync.data.api

import com.vishalsingh444888.sweatsync.data.model.Exercises
import retrofit2.http.GET

interface ExerciseApi {

    @GET("exercises")
    suspend fun getAllExercises(): Exercises

}