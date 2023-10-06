package com.vishalsingh444888.sweatsync.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vishalsingh444888.sweatsync.R
import com.vishalsingh444888.sweatsync.data.model.ExercisesItem
import com.vishalsingh444888.sweatsync.ui.viewmodel.AppViewModel

//@Preview(showSystemUi = true, showBackground = false)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewRoutineScreen(viewModel: AppViewModel, navController: NavController) {
    val routineTitle by viewModel.routineTitle.collectAsState()
    val context = LocalContext.current
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Create Routine",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                },
                actions = {

                    Text(text = "Save", fontSize = 14.sp, modifier = Modifier
                        .clickable {
                            if(routineTitle.isNotBlank() && !viewModel.checkForDuplicateRoutine(routineTitle)){
                                viewModel.addRoutineToFireStore(routineName = routineTitle)
                                viewModel.updateUserRoutineFromFireStore()
                                navController.navigate("Home")
                            }else if(routineTitle.isBlank()){
                                Toast.makeText(context,"Routine title can't be empty!",Toast.LENGTH_SHORT).show()
                            }else{
                                Toast.makeText(context,"Routine already exists with this name",Toast.LENGTH_SHORT).show()
                            }

                        }
                        .padding(end = 16.dp), color = MaterialTheme.colorScheme.primary)
                },
                navigationIcon = {
                    Text(text = "Cancel", fontSize = 14.sp, modifier = Modifier
                        .clickable {
                            navController.navigate("Home")
                        }
                        .padding(start = 16.dp), color = MaterialTheme.colorScheme.primary)
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(MaterialTheme.colorScheme.surface),
                modifier = Modifier.heightIn(max = 50.dp)
            )
        }
    ) { paddingValues ->
        val routineExercise by viewModel.routineExercises.collectAsState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(paddingValues)
        ) {
            var routineState by remember { mutableStateOf("") }
            TextField(
                value = routineState,
                onValueChange = {
                    routineState = it
                    viewModel.updateRoutineTitle(routineState)
                },
                placeholder = { Text(text = "Routine title") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent),
                modifier = Modifier.fillMaxWidth(),

                )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.new_routine_message),
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp),
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))

            // add Exercise button
            Button(
                onClick = {
                    navController.navigate("ExerciseList")
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Exercise")
                Text(text = "Add exercise")
            }
            Log.d("newRoutine", "${routineExercise.size}")
            if (routineExercise.size > 0) {
                LazyColumn(modifier = Modifier.padding(vertical = 16.dp)) {
                    items(routineExercise) { exercise ->
                        ExerciseWithSets(exercise = exercise, viewModel, onRemoveClick = {
                            viewModel.removeExerciseFromRoutineExercise(exercise)
                            viewModel.removeExerciseIdFromList(exercise.id)
                        }, navController)
                    }
                }
            }

        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseWithSets(
    exercise: ExercisesItem,
    viewModel: AppViewModel,
    onRemoveClick: () -> Unit,
    navController: NavController
) {
    var sets by remember { mutableStateOf("") }
    var reps by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(exercise.gifUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .height(40.dp)
                    .clip(CircleShape)
            )
            Spacer(Modifier.width(16.dp))
            Text(
                text = exercise.name,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = {
                onRemoveClick()
                navController.popBackStack("CreateNewRoutine", inclusive = true)
                navController.navigate("CreateNewRoutine")
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.close_48px),
                    contentDescription = "Delete Exercise",
                    Modifier.size(30.dp)
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),

            ) {
            TextField(
                value = sets,
                onValueChange = {
                    sets = it
                },
                placeholder = { Text(text = "Sets", fontSize = 16.sp) },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = { focusRequester.requestFocus() }),
                colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent),
                modifier = Modifier
                    .widthIn(max = 80.dp),
            )
            Spacer(modifier = Modifier.width(16.dp))
            TextField(
                value = reps,
                onValueChange = { reps = it },
                placeholder = { Text(text = "Reps", fontSize = 16.sp) },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    viewModel.addSetToRoutine(
                        exercise.id,
                        sets,
                        reps
                    )
                }),
                colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent),
                modifier = Modifier
                    .widthIn(max = 80.dp)
                    .focusRequester(focusRequester),
            )


        }

    }
}

data class ExerciseSet(val sets: String = "", val reps: String = "")
