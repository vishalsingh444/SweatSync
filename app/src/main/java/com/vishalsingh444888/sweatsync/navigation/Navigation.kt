package com.vishalsingh444888.sweatsync.navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vishalsingh444888.sweatsync.ui.auth.LoginScreen
import com.vishalsingh444888.sweatsync.ui.auth.RegisterScreen
import com.vishalsingh444888.sweatsync.ui.screens.ExerciseDetailsScreen
import com.vishalsingh444888.sweatsync.ui.screens.ExercisesScreen
import com.vishalsingh444888.sweatsync.ui.screens.HomeScreen
import com.vishalsingh444888.sweatsync.ui.screens.NewRoutineScreen
import com.vishalsingh444888.sweatsync.ui.screens.SweatSyncApp
import com.vishalsingh444888.sweatsync.ui.screens.ProfileScreen
import com.vishalsingh444888.sweatsync.ui.screens.StartRoutineScreen
import com.vishalsingh444888.sweatsync.ui.screens.WorkoutScreen
import com.vishalsingh444888.sweatsync.ui.viewmodel.AppViewModel
import com.vishalsingh444888.sweatsync.ui.viewmodel.RoutineData
import com.vishalsingh444888.sweatsync.ui.viewmodel.UiState

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun Navigation(viewModel: AppViewModel, uiState: UiState, navController: NavHostController) {

    NavHost(navController = navController, startDestination = "Home") {
        composable(route = "SignOut") {
            SweatSyncApp()
        }
        composable(route = "Home") {
            HomeScreen(viewModel, navController)
        }
        composable(route = "ExerciseList") {
            ExercisesScreen(uiState = uiState, viewModel = viewModel, navController)
        }
        composable(route = "Details") {
            ExerciseDetailsScreen(
                exercisesItem = (uiState as UiState.Success).currentExercise,
                navController
            )
        }
        composable(route = "Workout") {
            WorkoutScreen()
        }
        composable(route = "Profile") {
            ProfileScreen(viewModel, navController)
        }
        composable(route = "CreateNewRoutine") {
            NewRoutineScreen(viewModel = viewModel, navController)
        }
        composable(route = "StartRoutine"){
            if(viewModel.isStartRoutineListUpdated.value){
                StartRoutineScreen(viewModel = viewModel,navController)
                Log.d("appviewmodel","navigated to startroutinescreen")
            }

        }
    }
}

@Composable
fun AuthNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "Login") {
        composable(route = "Login") {
            LoginScreen(navController = navController)
        }
        composable(route = "Register") {
            RegisterScreen(navController = navController)
        }
        composable(route = "Home") {
            SweatSyncApp()
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Workout,
        BottomNavItem.Profile
    )
    val backStackEntry by navController.currentBackStackEntryAsState()
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = Color.LightGray,
        modifier = Modifier,
        tonalElevation = 0.dp
    ) {
        items.forEach { item ->
            val selected = backStackEntry?.destination?.route
            NavigationBarItem(
                selected = item.route == selected,
                onClick = { navController.navigate(item.route) },
                icon = {
                    Icon(painter = painterResource(id = item.icon), contentDescription = item.label)
                },
                label = {
                    Text(text = item.label)
                }
            )
        }
    }
}