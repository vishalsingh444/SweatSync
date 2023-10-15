package com.vishalsingh444888.sweatsync.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vishalsingh444888.sweatsync.data.model.ExercisesItem

@Dao
interface AppDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercises(exercisesItem: ExercisesItem)

    @Query("SELECT * FROM appDatabase")
    suspend fun getAllExercises(): List<ExercisesItem>

    @Query("SELECT * FROM appDatabase WHERE ID = :id")
    suspend fun getExerciseById(id : String): ExercisesItem

    @Query("DELETE FROM appDatabase")
    suspend fun deleteData()
}