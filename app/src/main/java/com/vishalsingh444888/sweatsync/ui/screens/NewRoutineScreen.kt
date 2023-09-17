package com.vishalsingh444888.sweatsync.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.vishalsingh444888.sweatsync.R
import com.vishalsingh444888.sweatsync.ui.viewmodel.AppViewModel

//@Preview(showSystemUi = true, showBackground = false)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewRoutineScreen(viewModel: AppViewModel, navController: NavController) {
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
                        .clickable { }
                        .padding(end = 16.dp), color = MaterialTheme.colorScheme.primary)
                },
                navigationIcon = {
                    Text(text = "Cancel", fontSize = 14.sp, modifier = Modifier
                        .clickable { navController.navigate("Home") }
                        .padding(start = 16.dp), color = MaterialTheme.colorScheme.primary)
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(MaterialTheme.colorScheme.surface),
                modifier = Modifier.heightIn(max = 50.dp)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(paddingValues)
        ) {
            var routineState by remember { mutableStateOf("") }
            TextField(
                value = routineState,
                onValueChange = { routineState = it },
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
        }
    }

}