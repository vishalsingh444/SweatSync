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