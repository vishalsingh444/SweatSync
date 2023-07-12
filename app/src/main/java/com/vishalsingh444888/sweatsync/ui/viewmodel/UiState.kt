package com.vishalsingh444888.sweatsync.ui.viewmodel

import com.vishalsingh444888.sweatsync.data.model.Exercises
import com.vishalsingh444888.sweatsync.data.model.ExercisesItem

sealed class UiState{
    object Loading: UiState()
    data class Success(val exercises: Exercises,val currentExercise: ExercisesItem = ExercisesItem("N/A","N/A","N/A","N/A","N/A","N/A")): UiState()
    data class Error(val message:String?): UiState()
}