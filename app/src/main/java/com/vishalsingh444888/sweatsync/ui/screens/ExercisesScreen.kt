package com.vishalsingh444888.sweatsync.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.vishalsingh444888.sweatsync.data.model.Exercises
import com.vishalsingh444888.sweatsync.ui.components.ExercisesList
import com.vishalsingh444888.sweatsync.ui.viewmodel.AppViewModel
import com.vishalsingh444888.sweatsync.ui.viewmodel.UiState

@Composable
fun ExercisesScreen(exercises: Exercises) {
    ExercisesList(exercises = exercises)
}