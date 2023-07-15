package com.vishalsingh444888.sweatsync.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.vishalsingh444888.sweatsync.data.model.Exercises
import com.vishalsingh444888.sweatsync.ui.components.ExercisesList
import com.vishalsingh444888.sweatsync.ui.viewmodel.AppViewModel

@Composable
fun ExercisesScreen(exercises: Exercises,viewModel: AppViewModel,navController: NavController) {
    ExercisesList(exercises = exercises, viewModel = viewModel, navController = navController)
}