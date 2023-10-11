package com.vishalsingh444888.sweatsync.ui.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.vishalsingh444888.sweatsync.R
import com.vishalsingh444888.sweatsync.ui.viewmodel.AppViewModel
import com.vishalsingh444888.sweatsync.ui.viewmodel.RoutineData

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: AppViewModel, navController: NavController) {
    viewModel.updateUserRoutineFromFireStore()
    val routines by viewModel.firebaseRoutine.collectAsState()
    val isStartRoutineListUpdated by viewModel.isStartRoutineListUpdated
    Log.d("appviewmodel","isStartRoutineListUpdated is $isStartRoutineListUpdated")
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Routines",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(MaterialTheme.colorScheme.surface),
                modifier = Modifier.heightIn(max = 50.dp)
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(16.dp)
        ) {
            Text(text = "Routines", fontWeight = FontWeight.SemiBold, fontSize = 24.sp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        navController.navigate("CreateNewRoutine")
                        viewModel.clearCurrentExerciseIds()
                        viewModel.clearRoutineExercises()
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        MaterialTheme.colorScheme.surface,
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    Row(Modifier.fillMaxWidth()) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_post_add_24),
                            contentDescription = "New Routine",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "New Routine", fontSize = 18.sp)
                    }
                }
                Spacer(Modifier.width(16.dp))
                Button(
                    onClick = {},
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        MaterialTheme.colorScheme.surface,
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    Row(Modifier.fillMaxWidth()) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_search_24),
                            contentDescription = "Explore",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(text = "Explore", fontSize = 18.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "My Routines", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(16.dp))
                Divider(thickness = 2.dp)
            }
            Spacer(modifier = Modifier.height(16.dp))

            RoutinesLazyList(routines = routines,viewModel,navController)
        }
    }
}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun RoutineCardComponent(
    routine: RoutineData,
    viewModel: AppViewModel,
    navController: NavController
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = routine.routineName,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "${routine.routine.size} Exercises",
                fontSize = 16.sp,
                fontWeight = FontWeight.Light,
                color = Color.LightGray
            )
            Button(onClick = {
                viewModel.resetStartRoutineList()
                viewModel.updateStartRoutineList(routine.routineName)
                viewModel.updateStartRoutine(routine)
                viewModel.clearCheckboxState()
                viewModel.startTimer()
                navController.navigate("StartRoutine")
            }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
                Text(text = "Start", fontSize = 16.sp)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun RoutinesLazyList(
    routines: List<RoutineData>,
    viewModel: AppViewModel,
    navController: NavController
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 60.dp)
    ) {
        items(routines) { routine ->
            RoutineCardComponent(routine = routine,viewModel,navController)
        }
    }
}