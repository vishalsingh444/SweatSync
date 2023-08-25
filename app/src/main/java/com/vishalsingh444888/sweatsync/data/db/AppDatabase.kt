package com.vishalsingh444888.sweatsync.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vishalsingh444888.sweatsync.data.model.Exercises
import com.vishalsingh444888.sweatsync.data.model.ExercisesItem

@Database(
    entities = [ExercisesItem::class],
    version = 2,

)
abstract class AppDatabase: RoomDatabase(){
    abstract fun appDao(): AppDao
}