package com.vishalsingh444888.sweatsync.ui.viewmodel

import com.vishalsingh444888.sweatsync.ui.screens.ExerciseSet

data class ProfileUiState(
    val username : String? = "User001",
    val profileUrl : String? = null,
)

data class ExerciseWithSet(
    val id: String = "0",
    val exerciseSet: ExerciseSet = ExerciseSet()
)

data class RoutineData(
    val routineName: String,
    val routine: List<ExerciseWithSet>
)

data class ExerciseCheckboxState(
    val exerciseId: String,
    val isChecked: Boolean
)

data class StartRoutineState(
    val duration: String = "00:00:00",
    val exercises: String = "0",
    val sets: String = "0"
)

data class Workout(
    val routineName: String,
    val duration: String,
    val exercises: String,
    val sets: String,
    val date: String
)