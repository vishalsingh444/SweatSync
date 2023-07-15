package com.vishalsingh444888.sweatsync.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.vishalsingh444888.sweatsync.data.model.Exercises
import com.vishalsingh444888.sweatsync.data.model.ExercisesItem
import com.vishalsingh444888.sweatsync.ui.viewmodel.AppViewModel

@Composable
fun ExerciseListComponent(
    viewModel: AppViewModel,
    exercise: ExercisesItem,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable {
                viewModel.updateCurrentExercise(exercise)
                navController.navigate("Details")
            }
            .padding(start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = exercise.name,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(text = exercise.bodyPart, fontSize = 16.sp, fontWeight = FontWeight.Light)
    }
    Divider(thickness = 2.dp, modifier = Modifier.fillMaxWidth())
}

@Composable
fun ExercisesList(viewModel: AppViewModel, exercises: Exercises,navController: NavController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(exercises) { exercise ->
            ExerciseListComponent(exercise = exercise, viewModel = viewModel, navController = navController)
        }
    }
}