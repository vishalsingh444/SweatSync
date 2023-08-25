package com.vishalsingh444888.sweatsync.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

//"bodyPart": "chest",
//"equipment": "body weight",
//"gifUrl": "https://edb-4rme8.ondigitalocean.app/image/MkPdY40SomYtLI",
//"id": "2363",
//"name": "wide-grip chest dip on high parallel bars",
//"target": "pectorals"
@Entity(tableName = "appDatabase")
data class ExercisesItem(
    val bodyPart: String,
    val equipment: String,
    val gifUrl: String,
    @PrimaryKey
    val id: String,
    val name: String,
    val target: String,
)
