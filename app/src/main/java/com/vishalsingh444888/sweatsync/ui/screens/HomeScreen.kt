package com.vishalsingh444888.sweatsync.ui.screens

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.vishalsingh444888.sweatsync.navigation.AuthNavigation
import com.vishalsingh444888.sweatsync.navigation.BottomNavigationBar
import com.vishalsingh444888.sweatsync.navigation.Navigation
import com.vishalsingh444888.sweatsync.ui.auth.AuthViewModel
import com.vishalsingh444888.sweatsync.ui.viewmodel.AppViewModel
import com.vishalsingh444888.sweatsync.ui.viewmodel.UiState
import kotlin.system.exitProcess

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: AppViewModel = hiltViewModel(),authViewModel: AuthViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val navController = rememberNavController()
    val authenticated by authViewModel.authenticated.collectAsState()
    if(authenticated){
        Scaffold (
            bottomBar = {
                BottomNavigationBar(navController = navController)
            }
        ){
            Navigation(viewModel = viewModel, uiState = uiState,navController = navController)
        }
    }
    else{
        AuthNavigation()
    }



}