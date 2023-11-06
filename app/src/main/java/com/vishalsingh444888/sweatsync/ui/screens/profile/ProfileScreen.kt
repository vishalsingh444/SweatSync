package com.vishalsingh444888.sweatsync.ui.screens.profile

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vishalsingh444888.sweatsync.ui.auth.AuthViewModel
import com.vishalsingh444888.sweatsync.ui.auth.restartApp
import com.vishalsingh444888.sweatsync.ui.screens.startRoutine.TopRowComponent
import com.vishalsingh444888.sweatsync.ui.viewmodel.Workout
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
) {
    val viewModel = hiltViewModel<AuthViewModel>()
    val profileViewModel = hiltViewModel<ProfileViewModel>()

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val profileUiState by profileViewModel.profileUiState.collectAsState()
    val workouts by profileViewModel.workoutList.collectAsState()
    val totalWorkout by profileViewModel.totalWorkouts.collectAsState()
    val totalExercises by profileViewModel.totalExercises.collectAsState()
    val totalSets by profileViewModel.totalSets.collectAsState()
    if (profileUiState.username == "User001") {
        profileViewModel.updateUserDetails()
    }
    if (workouts.isEmpty()) {
        profileViewModel.getWorkoutListFromFireStore()
    }
    Log.d("firebase", "name: ${profileUiState.username} ${profileUiState.profileUrl}")
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Profile",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                },
                modifier = Modifier.heightIn(max = 50.dp),
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(MaterialTheme.colorScheme.surface),
                actions = {
                    OutlinedButton(
                        onClick = {
                            scope.launch {
                                viewModel.signOut()
                            }
                            restartApp(context)
                            navController.navigate("SignOut")
                        },
                        modifier = Modifier.sizeIn(maxHeight = 35.dp, maxWidth = 95.dp)

                    ) {
                        Text(text = "Logout")
                    }
                }
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(16.dp)
        ) {
            UserProfileComp(
                name = profileUiState.username,
                profilePicture = profileUiState.profileUrl,
                workouts = totalWorkout,
                exercises = totalExercises,
                sets = totalSets
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Your workouts", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(16.dp))
                Divider(thickness = 2.dp)
            }


            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 70.dp)
            ) {
                items(workouts) {
                    WorkoutComponent(workout = it)
                }
            }

        }
    }

}

@Composable
fun UserProfileComp(
    name: String?,
    profilePicture: String?,
    workouts: String,
    exercises: String,
    sets: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        if (profilePicture != null) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(profilePicture)
                    .crossfade(true)
                    .build(),
                modifier = Modifier
                    .height(90.dp)
                    .clip(CircleShape),
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            if (name != null) {
                Text(text = name, fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                WorkOutDetailsComp(detailName = "Workouts", count = workouts)
                WorkOutDetailsComp(detailName = "Total Exe", count = exercises)
                WorkOutDetailsComp(detailName = "Total Sets", count = sets)
            }
        }

    }
}

@Composable
fun WorkOutDetailsComp(detailName: String, count: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = detailName, fontSize = 12.sp, color = Color.Gray)
        Text(text = count, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun SignOutScreen(navController: NavController, viewModel: AuthViewModel = hiltViewModel()) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(onClick = {
            scope.launch {
                viewModel.signOut()
            }
            restartApp(context)
            navController.navigate("SignOut")
        }) {
            Text(text = "Sign Out")
        }

    }
}

@Composable
fun WorkoutComponent(workout: Workout) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,

            ) {
            Text(
                text = workout.routineName,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = workout.date,
                fontSize = 16.sp,
                fontWeight = FontWeight.Light,
                fontStyle = FontStyle.Italic,
                color = Color.LightGray
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TopRowComponent(
                heading = "duration",
                value = workout.duration,
                color = Color.Gray,
                valueColor = Color.LightGray
            )
            TopRowComponent(
                heading = "exercises",
                value = workout.exercises,
                color = Color.Gray,
                valueColor = Color.LightGray
            )
            TopRowComponent(
                heading = "sets",
                value = workout.sets,
                color = Color.Gray,
                valueColor = Color.LightGray
            )
        }
        Divider(thickness = 2.dp)
    }
}

