package com.vishalsingh444888.sweatsync.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vishalsingh444888.sweatsync.data.model.Exercises
import com.vishalsingh444888.sweatsync.data.model.ExercisesItem

@Composable
fun ExerciseListComponent(exercise: ExercisesItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = exercise.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = exercise.bodyPart, fontSize = 16.sp, fontWeight = FontWeight.Light)
    }
    Divider(thickness = 2.dp)
}

@Composable
fun ExercisesList(exercises: Exercises) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ){
        items(exercises){exercise ->
            ExerciseListComponent(exercise = exercise)
        }
    }
}