package com.vishalsingh444888.sweatsync.ui.screens

import android.os.Build.VERSION.SDK_INT
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.vishalsingh444888.sweatsync.data.model.ExercisesItem

@Composable
fun ExerciseDetailsScreen(exercisesItem: ExercisesItem,navController: NavController) {
    BackHandler() {
        navController.popBackStack()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = exercisesItem.name, fontWeight = FontWeight.Bold, fontSize = 24.sp)
        Spacer(Modifier.height(8.dp))
        GifImage(gifUrl = exercisesItem.gifUrl)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Bodypart: "+exercisesItem.bodyPart,fontSize = 18.sp)
        Spacer(Modifier.height(8.dp))
        Text(text = "Target: "+exercisesItem.target,fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Equipment: "+exercisesItem.equipment,fontSize = 18.sp)
    }
}

@Composable
fun GifImage(gifUrl: String) {
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    Image(
        painter = rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current).data(gifUrl).build(),
            imageLoader
        ), contentDescription = "Gif Image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)),
    )
}

