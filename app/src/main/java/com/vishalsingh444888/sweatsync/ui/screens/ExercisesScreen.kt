package com.vishalsingh444888.sweatsync.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.vishalsingh444888.sweatsync.ui.auth.SignOutScreen
import com.vishalsingh444888.sweatsync.ui.components.ExercisesList
import com.vishalsingh444888.sweatsync.ui.viewmodel.AppViewModel
import com.vishalsingh444888.sweatsync.ui.viewmodel.UiState

@Composable
fun ExercisesScreen(uiState: UiState,viewModel: AppViewModel,navController: NavController) {
    when(uiState){
        is UiState.Error -> {
                null
//            viewModel.fetchData()
        }
        UiState.Loading -> {
            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,verticalArrangement = Arrangement.Center){
                CircularProgressIndicator()
            }
        }
        is UiState.Success -> {
            ExercisesList(exercises = uiState.exercises, viewModel = viewModel, navController = navController)
        }
    }


}