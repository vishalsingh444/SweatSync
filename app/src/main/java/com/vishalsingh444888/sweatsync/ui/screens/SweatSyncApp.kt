package com.vishalsingh444888.sweatsync.ui.screens

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vishalsingh444888.sweatsync.navigation.AuthNavigation
import com.vishalsingh444888.sweatsync.navigation.BottomNavigationBar
import com.vishalsingh444888.sweatsync.navigation.Navigation
import com.vishalsingh444888.sweatsync.ui.auth.AuthViewModel
import com.vishalsingh444888.sweatsync.ui.viewmodel.AppViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SweatSyncApp(viewModel: AppViewModel = hiltViewModel(), authViewModel: AuthViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val navController = rememberNavController()
    val authenticated by authViewModel.authenticated.collectAsState()
    var showBottomBar by rememberSaveable { mutableStateOf(true) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    showBottomBar = when(navBackStackEntry?.destination?.route){
        "CreateNewRoutine" -> false
        "ExerciseList" -> false
        "Details" -> false
        else -> true
    }
    if(authenticated){
        Scaffold (
            bottomBar = {
                if(showBottomBar) BottomNavigationBar(navController = navController)
            }
        ){
            Navigation(viewModel = viewModel, uiState = uiState,navController = navController)
        }
    }
    else{
        AuthNavigation()
    }
}

