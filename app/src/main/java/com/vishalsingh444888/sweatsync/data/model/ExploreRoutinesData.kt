package com.vishalsingh444888.sweatsync.data.model

import com.vishalsingh444888.sweatsync.ui.screens.ExerciseSet
import com.vishalsingh444888.sweatsync.ui.viewmodel.ExerciseWithSet
import com.vishalsingh444888.sweatsync.ui.viewmodel.RoutineData

val classic_3_day_split = listOf<RoutineData>(
    RoutineData(
        routineName = "1. Chest Shoulders and Triceps",
        routine = listOf(
            ExerciseWithSet(id = "0025", ExerciseSet(sets = "4", reps = "8")),
            ExerciseWithSet(id = "0404", ExerciseSet(sets = "4", reps = "8")),
            ExerciseWithSet(id = "0362", ExerciseSet(sets = "3", reps = "8")),
            ExerciseWithSet(id = "0047", ExerciseSet(sets = "4", reps = "8")),
            ExerciseWithSet(id = "0334", ExerciseSet(sets = "3", reps = "8")),
            ExerciseWithSet(id = "0241", ExerciseSet(sets = "3", reps = "8")),
        )
    ),
    RoutineData(
        routineName = "2. Back and Bicep",
        routine = listOf(
            ExerciseWithSet(id = "0160", ExerciseSet(sets = "4", reps = "8")),
            ExerciseWithSet(id = "0150", ExerciseSet(sets = "4", reps = "8")),
            ExerciseWithSet(id = "0294", ExerciseSet(sets = "3", reps = "8")),
            ExerciseWithSet(id = "0573", ExerciseSet(sets = "3", reps = "8")),
            ExerciseWithSet(id = "0160", ExerciseSet(sets = "3", reps = "8")),
            ExerciseWithSet(id = "2402", ExerciseSet(sets = "3", reps = "8")),
        )
    ),
    RoutineData(
        routineName = "3. Legs and Abs",
        routine = listOf(
            ExerciseWithSet(id = "0026", ExerciseSet(sets = "4", reps = "8")),
            ExerciseWithSet(id = "3235", ExerciseSet(sets = "3", reps = "8")),
            ExerciseWithSet(id = "0585", ExerciseSet(sets = "3", reps = "8")),
            ExerciseWithSet(id = "0088", ExerciseSet(sets = "3", reps = "8")),
            ExerciseWithSet(id = "2135", ExerciseSet(sets = "3", reps = "1.30min")),
            ExerciseWithSet(id = "0872", ExerciseSet(sets = "3", reps = "8")),
        )
    )
)