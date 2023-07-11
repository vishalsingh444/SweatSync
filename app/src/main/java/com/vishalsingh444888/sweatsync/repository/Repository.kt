package com.vishalsingh444888.sweatsync.repository

import com.vishalsingh444888.sweatsync.data.api.ExerciseApi
import com.vishalsingh444888.sweatsync.data.model.Exercises

interface Repository {
    suspend fun getAllExercise(): Exercises
}

class NetworkRepository(
    private val Api: ExerciseApi
):Repository{
    override suspend fun getAllExercise(): Exercises {
        return Api.getAllExercises()
    }
}