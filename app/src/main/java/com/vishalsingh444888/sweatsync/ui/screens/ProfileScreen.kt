package com.vishalsingh444888.sweatsync.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ProfileScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("Profile",Modifier.align(Alignment.CenterHorizontally))
    }
}