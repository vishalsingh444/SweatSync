package com.vishalsingh444888.sweatsync.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.vishalsingh444888.sweatsync.ui.components.ExerciseListComponent
import com.vishalsingh444888.sweatsync.ui.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedExploreRoutineScreen(navController: NavController,viewModel: AppViewModel) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {},
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(MaterialTheme.colorScheme.surface),
                modifier = Modifier.heightIn(max = 50.dp),
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("Workout") }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
                    }
                }
            )
        }
    ) {contentPadding ->
        val exercises by viewModel.exploreRoutineList.collectAsState()
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ){
            LazyColumn(
                modifier = Modifier
            ){
                items(exercises){exercise ->
                    ExerciseListComponent(viewModel = viewModel, exercise = exercise, navController = navController, isCheckRequired = false)
                }
            }
        }

    }
}

