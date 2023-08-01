package com.vishalsingh444888.sweatsync.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vishalsingh444888.sweatsync.ui.auth.LoginScreen
import com.vishalsingh444888.sweatsync.ui.screens.ExerciseDetailsScreen
import com.vishalsingh444888.sweatsync.ui.screens.ExercisesScreen
import com.vishalsingh444888.sweatsync.ui.viewmodel.AppViewModel
import com.vishalsingh444888.sweatsync.ui.viewmodel.UiState

@Composable
fun Navigation(viewModel: AppViewModel,uiState: UiState) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "Login"){
        composable(route = "Login"){
            LoginScreen(navController = navController)
        }
        composable(route = "Home"){
            ExercisesScreen(exercises = (uiState as UiState.Success).exercises, viewModel = viewModel,navController )
        }
        composable(route = "Details"){
            ExerciseDetailsScreen(exercisesItem = (uiState as UiState.Success).currentExercise,navController)
        }
    }
}