package com.vishalsingh444888.sweatsync.navigation

import com.vishalsingh444888.sweatsync.R

sealed class BottomNavItem(val icon: Int,val label : String, val route: String){
    object Home : BottomNavItem(icon = R.drawable.home_48px, label = "Home", route = "Home")
    object Workout: BottomNavItem(icon = R.drawable.exercise_48px, label = "Workout",route = "Workout")
    object Profile : BottomNavItem(icon = R.drawable.person_48px, label = "Profile", route = "Profile")

}
