package com.vishalsingh444888.sweatsync.ui.screens.startRoutine

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.vishalsingh444888.sweatsync.data.model.ExercisesItem
import com.vishalsingh444888.sweatsync.repository.Repository
import com.vishalsingh444888.sweatsync.ui.viewmodel.ExerciseCheckboxState
import com.vishalsingh444888.sweatsync.ui.viewmodel.RoutineData
import com.vishalsingh444888.sweatsync.ui.viewmodel.StartRoutineState
import com.vishalsingh444888.sweatsync.ui.viewmodel.Workout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer

@HiltViewModel
class StartRoutineViewModel @Inject constructor(
    private val repository: Repository,
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
):ViewModel(){
    private val _startRoutineList = MutableStateFlow(mutableListOf<ExercisesItem>())
    val startRoutineList = _startRoutineList.asStateFlow()

    private val _startRoutine = MutableStateFlow(RoutineData(routineName = "",routine = listOf()))
    val startRoutine = _startRoutine.asStateFlow()

    private val _startRoutineState = MutableStateFlow(StartRoutineState())
    val startRoutineState = _startRoutineState.asStateFlow()

    private val checkboxState = mutableStateListOf<ExerciseCheckboxState>()

    private val _isStartRoutineListUpdated = mutableStateOf(false)
    val isStartRoutineListUpdated = _isStartRoutineListUpdated



    //timer variables
    @RequiresApi(Build.VERSION_CODES.O)
    private var time: Duration = Duration.ZERO
    private lateinit var timer: Timer

    var seconds by mutableStateOf("00")
    var minutes by mutableStateOf("00")
    var hours by mutableStateOf("00")
    var isPlaying by mutableStateOf(false)

    @RequiresApi(Build.VERSION_CODES.O)
    val currentDate = LocalDate.now()

    @RequiresApi(Build.VERSION_CODES.O)
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yy")

    @RequiresApi(Build.VERSION_CODES.O)
    val formateDate = currentDate.format(formatter)

    @RequiresApi(Build.VERSION_CODES.S)
    fun startTimer(){
        timer = fixedRateTimer(initialDelay = 1000L, period = 1000L){
            time = time.plus(Duration.ofSeconds(1L))
            updateTimeState()
        }
        isPlaying = true
        Log.d("timer","timer started")
    }
    fun pauseTimer(){
        timer.cancel()
        isPlaying = false
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun restartTimer(){
        pauseTimer()
        time = Duration.ZERO
        updateTimeState()
    }
    fun resetStartRoutineList(){
        _startRoutineList.value.clear()
        Log.d("appviewmodel","_startroutinList size = ${_startRoutineList.value.size}")
    }
    fun resetStartRoutineState(){
        _startRoutineState.value = _startRoutineState.value.copy(duration = "0:0:0", exercises = "0", sets = "0")
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun updateTimeState(){
        this.seconds = time.toSecondsPart().toString()
        this.minutes = time.toMinutesPart().toString()
        this.hours = time.toHoursPart().toString()
        val duration = "$hours:$minutes:$seconds"
        Log.d("timer","current duration = $duration")
        updateDurationInStartRoutineState(duration = duration)
    }

    fun updateDurationInStartRoutineState(duration: String){
        _startRoutineState.value = _startRoutineState.value.copy(
            duration = duration
        )
        Log.d("timer", "in _startRoutineState the duration in ${_startRoutineState.value.duration}")
    }

    fun updateWorkoutToFireStore(routineName:String,duration: String, exercises: String, sets: String,date: String){
        val workout = Workout(routineName,duration,exercises,sets,date)
        viewModelScope.launch {
            val uid = firebaseAuth.currentUser?.uid
            val userDocument = firestore.collection("users").document(uid?:"")

            userDocument.update("workouts", FieldValue.arrayUnion(workout))
                .addOnSuccessListener {
                    Log.d("firebase","workout successfully added")
                    getWorkoutListFromFireStore()
                }
                .addOnFailureListener { e ->
                    Log.d("firebase","error: $e")
                }
        }
    }

    // to check the checkbox state for lazyColumn in start routine screen
    fun getCheckboxState(exerciseId:String):Boolean{
        val state = checkboxState.find { it.exerciseId == exerciseId }
        return state?.isChecked?:false
    }

    fun setCheckboxState(exerciseId: String,isChecked: Boolean){
        val index = checkboxState.indexOfFirst{ it.exerciseId == exerciseId }
        if(index!=-1){
            checkboxState[index] = checkboxState[index].copy(isChecked = isChecked)
        }
        else{
            checkboxState.add(ExerciseCheckboxState(exerciseId = exerciseId,isChecked = isChecked))
        }
    }

    fun updateStartRoutineState(exercises: String, sets: String){
        val _exercise = _startRoutineState.value.exercises.toInt() + exercises.toInt()
        val _set = _startRoutineState.value.sets.toInt() + sets.toInt()

        _startRoutineState.value = _startRoutineState.value.copy(
            exercises = _exercise.toString(),
            sets = _set.toString()
        )
    }

    fun clearCheckboxState(){
        checkboxState.clear()
    }

    fun updateStartRoutineList(routineName: String,_firebaseRoutine:List<RoutineData>){
        _isStartRoutineListUpdated.value = false
        val startRoutine = _firebaseRoutine.find { it.routineName == routineName }
        if(startRoutine!=null){
            viewModelScope.launch {
                startRoutine.routine.forEach{
                    _startRoutineList.value.add(repository.getExerciseById(it.id))
                    Log.d("appviewmodel","_startRoutineList size = ${_startRoutineList.value.size}")
                }
                _isStartRoutineListUpdated.value = true
            }

        }
    }

    fun updateStartRoutine(routine: RoutineData){
        _startRoutine.value = routine
        Log.d("appviewmodel","startRoutine is ${startRoutine.value.routineName}")
    }

}