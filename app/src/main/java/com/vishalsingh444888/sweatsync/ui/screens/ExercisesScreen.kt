package com.vishalsingh444888.sweatsync.ui.screens

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.vishalsingh444888.sweatsync.ui.auth.SignOutScreen
import com.vishalsingh444888.sweatsync.ui.components.ExercisesList
import com.vishalsingh444888.sweatsync.ui.viewmodel.AppViewModel
import com.vishalsingh444888.sweatsync.ui.viewmodel.UiState

@Composable
fun ExercisesScreen(uiState: UiState,viewModel: AppViewModel,navController: NavController) {
    when(uiState){
        is UiState.Error -> TODO()
        UiState.Loading -> {
            CircularProgressIndicator()
        }
        is UiState.Success -> {
            ExercisesList(exercises = uiState.exercises, viewModel = viewModel, navController = navController)
        }
    }

    SignOutScreen(navController = navController)
}