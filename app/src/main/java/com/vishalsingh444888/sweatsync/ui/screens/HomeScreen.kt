package com.vishalsingh444888.sweatsync.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.vishalsingh444888.sweatsync.ui.viewmodel.AppViewModel
import com.vishalsingh444888.sweatsync.ui.viewmodel.UiState

@Composable
fun HomeScreen(viewModel: AppViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    when(uiState){
        is UiState.Error -> TODO()
        UiState.Loading -> {
            Text(text = "Loading...")
        }
        is UiState.Success -> {
            val exercises = (uiState as UiState.Success).exercises
            ExercisesScreen(exercises = exercises)
        }
    }
}