package com.vishalsingh444888.sweatsync.ui.screens

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vishalsingh444888.sweatsync.ui.auth.SignOutScreen
import com.vishalsingh444888.sweatsync.ui.viewmodel.AppViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(appViewModel: AppViewModel, navController: NavController) {
    val profileUiState = appViewModel.profileUiState.collectAsState()
    if (profileUiState.value.username == "User001") {
        appViewModel.updateUserDetails()
    }
    Log.d("firebase", "name: ${profileUiState.value.username} ${profileUiState.value.profileUrl}")
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Profile", fontWeight = FontWeight.SemiBold) },
                modifier = Modifier.heightIn(max = 50.dp),
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(MaterialTheme.colorScheme.surface)
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            UserProfileComp(
                name = profileUiState.value.username,
                profilePicture = profileUiState.value.profileUrl
            )
            SignOutScreen(navController = navController)
        }
    }

}

@Composable
fun UserProfileComp(name: String?, profilePicture: String?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
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
                WorkOutDetailsComp(detailName = "Workouts", count = "0")
                WorkOutDetailsComp(detailName = "Total Exe", count = "0")
                WorkOutDetailsComp(detailName = "Total Sets", count = "0")
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