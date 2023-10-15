package com.vishalsingh444888.sweatsync.repository

import com.vishalsingh444888.sweatsync.data.api.ExerciseApi
import com.vishalsingh444888.sweatsync.data.db.AppDao
import com.vishalsingh444888.sweatsync.data.model.Exercises
import com.vishalsingh444888.sweatsync.data.model.ExercisesItem

interface Repository {
    suspend fun getAllExercise(): Exercises

    suspend fun insertExerciseInDb(exercisesItem: ExercisesItem)

    suspend fun getAllExerciseFromDb(): List<ExercisesItem>

    suspend fun getExerciseById(id: String): ExercisesItem

    suspend fun deleteData()
}

class NetworkRepository(
    private val api: ExerciseApi,
    private val dao: AppDao
):Repository{
    override suspend fun getAllExercise(): Exercises {
        return api.getAllExercises()
    }

    override suspend fun deleteData() {
        dao.deleteData()
    }

    override suspend fun getExerciseById(id: String): ExercisesItem {
        return dao.getExerciseById(id = id)
    }

    override suspend fun insertExerciseInDb(exercisesItem: ExercisesItem) {
        return dao.insertExercises(exercisesItem)
    }

    override suspend fun getAllExerciseFromDb(): List<ExercisesItem> {
        return dao.getAllExercises()
    }


}