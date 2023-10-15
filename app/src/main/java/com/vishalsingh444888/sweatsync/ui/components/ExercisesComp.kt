package com.vishalsingh444888.sweatsync.ui.components

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vishalsingh444888.sweatsync.R
import com.vishalsingh444888.sweatsync.data.model.Exercises
import com.vishalsingh444888.sweatsync.data.model.ExercisesItem
import com.vishalsingh444888.sweatsync.ui.viewmodel.AppViewModel

@Composable
fun ExerciseListComponent(
    viewModel: AppViewModel,
    exercise: ExercisesItem,
    navController: NavController,
    isCheckRequired: Boolean = true
) {
    val exerciseIds by viewModel.exerciseIds.collectAsState()
    var iconState by remember { mutableStateOf(exerciseIds.contains(exercise.id)) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable {
                viewModel.updateCurrentExercise(exercise)
                navController.navigate("Details")
            }
            .padding(start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(exercise.gifUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .height(60.dp)
                .clip(CircleShape),
            onError = {
                viewModel.fetchData()
            }

        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp, end = 8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = exercise.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = exercise.bodyPart,
                fontSize = 16.sp,
                fontWeight = FontWeight.Light,
                color = Color.Gray
            )
        }
        if(isCheckRequired){
            IconButton(onClick = {
                iconState = !iconState
                if (iconState) {
                    viewModel.addExerciseIdToList(exercise.id)

                } else {
                    viewModel.removeExerciseIdFromList(exercise.id)
                }

            }) {
                Icon(
                    painter = if (exerciseIds.contains(exercise.id)) {
                        painterResource(id = R.drawable.close_48px)
                    } else {
                        painterResource(id = R.drawable.add_48px)
                    }, contentDescription = if (iconState) "Remove Exercise" else "add Exercise",
                    tint = if (exerciseIds.contains(exercise.id)) {
                        Color.Red
                    } else {
                        MaterialTheme.colorScheme.primary
                    }
                )
            }
        }
    }

    Divider(
        thickness = 1.dp, modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
    )
}

@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchExercise() {
    var searchValue by remember { mutableStateOf("") }

    OutlinedTextField(
        value = searchValue,
        onValueChange = { searchValue = it },
        placeholder = { Text(text = "Search exercise") },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_search_24),
                contentDescription = "Search exercise",
                modifier = Modifier.size(28.dp)
            )
        },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
            .heightIn(max = 50.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                TODO()
            }
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = MaterialTheme.colorScheme.surface,
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Color.Transparent
        ),
        shape = RoundedCornerShape(8.dp)
    )
}

@Composable
fun ExercisesList(viewModel: AppViewModel, exercises: Exercises, navController: NavController) {
    val exerciseIdsList by viewModel.exerciseIds.collectAsState()
    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxSize()) {
        val exerciseIdsList by viewModel.exerciseIds.collectAsState()
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            SearchExercise()
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(exercises) { exercise ->
                    ExerciseListComponent(
                        exercise = exercise,
                        viewModel = viewModel,
                        navController = navController
                    )
                }
            }

        }
        Button(
            onClick = {
                      if(exerciseIdsList.size>0){
                          viewModel.addExercisesToRoutineExercises()
                          navController.navigate("CreateNewRoutine")
                      }else{
                          Toast.makeText(context," Select atleast 1 exercise",Toast.LENGTH_SHORT).show()
                      }
                navController.popBackStack("ExerciseList", inclusive = true)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(
                    Alignment.BottomCenter
                )
                .heightIn(max = 40.dp),
        ) {
            Text(text = "add Exercises")
        }
    }

}