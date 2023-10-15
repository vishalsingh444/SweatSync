package com.vishalsingh444888.sweatsync.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.vishalsingh444888.sweatsync.R
import com.vishalsingh444888.sweatsync.data.model.classic_3_day_split
import com.vishalsingh444888.sweatsync.ui.viewmodel.AppViewModel
import com.vishalsingh444888.sweatsync.ui.viewmodel.RoutineData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutScreen(navController: NavController,viewModel: AppViewModel) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Explore Routines",
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
            Text(text = "Plans", fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
            Text(
                text = stringResource(id = R.string.explore_routines_message),
                fontSize = 18.sp,
                color = androidx.compose.ui.graphics.Color.Gray
            )
            Text(text = "Classic 3 Day Split", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            LazyRow(
                modifier = Modifier
            ) {
                items(classic_3_day_split) { routine ->
                    ExploreRoutineComponent(routine = routine, navController = navController, viewModel = viewModel)
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreRoutineComponent(routine: RoutineData,navController: NavController,viewModel: AppViewModel) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .sizeIn(minHeight = 120.dp, maxHeight = 100.dp, minWidth = 160.dp, maxWidth = 160.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
        onClick = {
            viewModel.updateExploreRoutines(routine)
            navController.navigate("DetailedExploreRoutine")
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp,top = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            Text(text = routine.routineName, fontSize = 16.sp, modifier = Modifier.weight(1f), overflow = TextOverflow.Ellipsis)
            IconButton(
                onClick = { Toast.makeText(
                context,
                "routine added",
                Toast.LENGTH_SHORT
            ).show()
                viewModel.addRoutineToFireStore(routineName = routine.routineName, routineList = routine.routine) }) {
                Icon(
                    painter = painterResource(id = R.drawable.add_48px),
                    contentDescription = "add routine",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}