package com.vishalsingh444888.sweatsync.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.vishalsingh444888.sweatsync.ui.components.ExercisesList
import com.vishalsingh444888.sweatsync.ui.viewmodel.AppViewModel
import com.vishalsingh444888.sweatsync.ui.viewmodel.UiState

@Composable
fun ExercisesScreen(uiState: UiState,viewModel: AppViewModel,navController: NavController) {
    when(uiState){
        is UiState.Error -> {
                Toast.makeText(LocalContext.current,"${uiState.message}",Toast.LENGTH_LONG).show()
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