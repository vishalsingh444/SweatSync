package com.vishalsingh444888.sweatsync.ui.screens

import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vishalsingh444888.sweatsync.data.model.ExercisesItem
import com.vishalsingh444888.sweatsync.ui.viewmodel.AppViewModel
import com.vishalsingh444888.sweatsync.ui.viewmodel.ExerciseWithSet

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartRoutineScreen(viewModel: AppViewModel, navController: NavController) {
    val exercises by viewModel.startRoutineList.collectAsState()
    Log.d("appviewmodel", "exercises size in StartRoutineScreen = ${exercises.size}")
    val routine by viewModel.startRoutine.collectAsState()
    Log.d("appviewmodel", "routine in StartRoutineScreen is ${routine.routine.size}")
    val uiState by viewModel.startRoutineState.collectAsState()
    val isTimerPlaying by remember {
        mutableStateOf(viewModel.isPlaying)
    }
    BackHandler {
        viewModel.restartTimer()
        viewModel.resetStartRoutineState()
        navController.navigate("Home")
    }
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = "Log Workout", fontWeight = FontWeight.SemiBold, fontSize = 18.sp
            )
        }, actions = {
            Button(
                onClick = {
                    viewModel.updateWorkoutToFireStore(
                        routine.routineName,
                        uiState.duration,
                        uiState.exercises,
                        uiState.sets,
                        viewModel.formateDate

                    )
                    navController.navigate("Home")
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .sizeIn(maxWidth = 95.dp, maxHeight = 35.dp)
                    .padding(end = 8.dp)
            ) {
                Text(text = "Finish")
            }

        })
    }) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TopRowComponent(heading = "duration", value = uiState.duration, onClick = {})
                TopRowComponent(heading = "Exercises", value = uiState.exercises)
                TopRowComponent(heading = "Sets", value = uiState.sets)
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(exercises) {
                    ExerciseSetComponent(exercise = it, routine = routine.routine, viewModel)
                }
            }

        }
    }
}

@Composable
fun TopRowComponent(
    heading: String,
    value: String,
    fontWeight: FontWeight = FontWeight.Normal,
    color: Color = MaterialTheme.colorScheme.onBackground,
    onClick: () -> Unit = {}

) {
    Column(
        modifier = Modifier.clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = heading, fontSize = 12.sp, fontWeight = fontWeight, color = color)
        Text(text = value, fontSize = 12.sp, fontWeight = fontWeight, color = color)
    }
}

@Composable
fun ExerciseSetComponent(
    exercise: ExercisesItem, routine: List<ExerciseWithSet>, viewModel: AppViewModel
) {
    var isChecked by remember {
        mutableStateOf(viewModel.getCheckboxState(exercise.id))
    }
    val currentExercise = routine.find { it.id == exercise.id }
    Column(modifier = Modifier.fillMaxWidth()) {
        Divider(thickness = 1.dp)
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(exercise.gifUrl)
                    .crossfade(true).build(),
                contentDescription = null,
                modifier = Modifier
                    .height(40.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = exercise.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                maxLines = 2,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TopRowComponent(
                heading = "bodypart",
                value = exercise.bodyPart,
                fontWeight = FontWeight.Light,
                color = Color.Gray
            )
            TopRowComponent(
                heading = "target",
                value = exercise.target,
                fontWeight = FontWeight.Light,
                color = Color.Gray
            )
            TopRowComponent(
                heading = "equipment",
                value = exercise.equipment,
                fontWeight = FontWeight.Light,
                color = Color.Gray
            )
        }
        if (currentExercise != null) {
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = currentExercise.exerciseSet.sets + " x " + currentExercise.exerciseSet.reps,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier.weight(1f),
                    color = Color.Gray
                )
                Checkbox(checked = isChecked, onCheckedChange = {
                    isChecked = it
                    viewModel.setCheckboxState(exerciseId = exercise.id, it)
                    viewModel.updateStartRoutineState(
                        exercises = "1",
                        sets = "${currentExercise.exerciseSet.sets}"
                    )
                })
            }
        }

    }
}
