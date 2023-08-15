package com.vishalsingh444888.sweatsync.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vishalsingh444888.sweatsync.ui.auth.LoginScreen
import com.vishalsingh444888.sweatsync.ui.auth.RegisterScreen
import com.vishalsingh444888.sweatsync.ui.screens.ExerciseDetailsScreen
import com.vishalsingh444888.sweatsync.ui.screens.ExercisesScreen
import com.vishalsingh444888.sweatsync.ui.screens.HomeScreen
import com.vishalsingh444888.sweatsync.ui.screens.ProfileScreen
import com.vishalsingh444888.sweatsync.ui.screens.WorkoutScreen
import com.vishalsingh444888.sweatsync.ui.viewmodel.AppViewModel
import com.vishalsingh444888.sweatsync.ui.viewmodel.UiState

@Composable
fun Navigation(viewModel: AppViewModel,uiState: UiState,navController: NavHostController) {

    NavHost(navController = navController, startDestination = "Home"){
        composable(route = "SignOut"){
            HomeScreen()
        }
        composable(route = "Home"){
            ExercisesScreen(uiState = uiState, viewModel = viewModel,navController )
        }
        composable(route = "Details"){
            ExerciseDetailsScreen(exercisesItem = (uiState as UiState.Success).currentExercise,navController)
        }
        composable(route = "Workout"){
            WorkoutScreen()
        }
        composable(route = "Profile"){
            ProfileScreen()
        }
    }
}

@Composable
fun AuthNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "Login"){
        composable(route = "Login"){
            LoginScreen(navController = navController)
        }
        composable(route = "Register"){
            RegisterScreen(navController = navController)
        }
        composable(route = "Home"){
            HomeScreen()
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
        containerColor = MaterialTheme.colorScheme.background
    ) {
        items.forEach{  item->
            val selected = backStackEntry?.destination?.route
            NavigationBarItem(
                selected = item.route == selected,
                onClick = { navController.navigate(item.route) },
                icon = {
                    Icon(painter = painterResource(id = item.icon), contentDescription = item.label)
                },
                label = {
                    Text(text = item.label, fontWeight = FontWeight.SemiBold)
                }
            )
        }
    }
}