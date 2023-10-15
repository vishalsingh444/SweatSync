package com.vishalsingh444888.sweatsync.ui.viewmodel

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
import com.vishalsingh444888.sweatsync.data.model.Exercises
import com.vishalsingh444888.sweatsync.data.model.ExercisesItem
import com.vishalsingh444888.sweatsync.repository.Repository
import com.vishalsingh444888.sweatsync.ui.screens.ExerciseSet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer

@HiltViewModel
class AppViewModel @Inject constructor(
    private val repository: Repository,
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
): ViewModel() {

    private val _profileUiState = MutableStateFlow(ProfileUiState())
    val profileUiState = _profileUiState
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> get() = _uiState
    var data = Exercises()

    val exerciseIds =  MutableStateFlow(mutableListOf<String>())
    val routine = MutableStateFlow(mutableListOf<ExerciseWithSet>())

    private val _routineExercises = MutableStateFlow(mutableListOf<ExercisesItem>())
    val routineExercises = _routineExercises.asStateFlow()

    private val _routineTitle = MutableStateFlow<String>("")
    val routineTitle = _routineTitle.asStateFlow()

    private val _firebaseRoutine = MutableStateFlow(listOf<RoutineData>())
    val firebaseRoutine = _firebaseRoutine.asStateFlow()

    private val _startRoutineList = MutableStateFlow(mutableListOf<ExercisesItem>())
    val startRoutineList = _startRoutineList.asStateFlow()

    val startRoutine = MutableStateFlow(RoutineData(routineName = "",routine = listOf()))

    private val _isStartRoutineListUpdated = mutableStateOf(false)
    val isStartRoutineListUpdated = _isStartRoutineListUpdated

    private val checkboxState = mutableStateListOf<ExerciseCheckboxState>()

    private val _startRoutineState = MutableStateFlow(StartRoutineState())
    val startRoutineState = _startRoutineState.asStateFlow()

    private val _workoutList = MutableStateFlow<List<Workout>>(listOf())
    val workoutList = _workoutList.asStateFlow()

    private val _totalWorkouts = MutableStateFlow<String>("")
    val totalWorkouts = _totalWorkouts.asStateFlow()

    private val _totalExercises = MutableStateFlow("")
    val totalExercises = _totalExercises.asStateFlow()

    private val _totalSets = MutableStateFlow("")
    val totalSets = _totalSets.asStateFlow()

    private val _exploreRoutineList = MutableStateFlow(mutableListOf<ExercisesItem>())
    val exploreRoutineList = _exploreRoutineList.asStateFlow()
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

    init{
        viewModelScope.launch {
            storeDataInDatabase()
        }
        _routineExercises.value.clear()

    }

    fun updateExploreRoutines(routine: RoutineData){
        val exercises = mutableListOf<ExercisesItem>()
        viewModelScope.launch {
            routine.routine.forEach {
                val exercise = repository.getExerciseById(it.id)
                exercises.add(exercise)
            }
            if(exercises.isNotEmpty()){
                _exploreRoutineList.value = exercises
            }
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
    fun resetStartRoutineState(){
        _startRoutineState.value = _startRoutineState.value.copy(duration = "0:0:0", exercises = "0", sets = "0")
    }
    fun updateDurationInStartRoutineState(duration: String){
        _startRoutineState.value = _startRoutineState.value.copy(
            duration = duration
        )
        Log.d("timer", "in _startRoutineState the duration in ${_startRoutineState.value.duration}")
    }
    // to check the checkbox state for lazyColumn in start routine screen
    fun getCheckboxState(exerciseId:String):Boolean{
        val state = checkboxState.find { it.exerciseId == exerciseId }
        return state?.isChecked?:false
    }
    fun clearCheckboxState(){
        checkboxState.clear()
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

    fun updateStartRoutine(routine: RoutineData){
        startRoutine.value = routine
        Log.d("appviewmodel","startRoutine is ${startRoutine.value.routineName}")
    }
    fun fetchData(){
        viewModelScope.launch {
            repository.deleteData()
            storeDataInDatabase()
        }
    }
    private suspend fun storeDataInDatabase(){
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val dbData = repository.getAllExerciseFromDb()
            Log.d("appViewModel", "dbData: ${dbData.isEmpty()}")
            if(dbData.isEmpty()){
                try{
                    data = repository.getAllExercise()
                }catch (e:Exception){
                    _uiState.value = UiState.Error(e.message)
                }
                Log.d("appViewModel","data: ${data.size}")
                data.forEach { exercisesItem ->
                    repository.insertExerciseInDb(exercisesItem)
                }
            }else{
                val list = repository.getAllExerciseFromDb()
                data.addAll(list)
            }
            if(!data.isEmpty()){
                _uiState.value = UiState.Success(data,data[0])
            }
        }
    }
    fun updateCurrentExercise(exercisesItem: ExercisesItem){
        _uiState.value = UiState.Success(data,exercisesItem)
    }

    fun updateUserDetails(){
        viewModelScope.launch {
            val usersCollection  = firestore.collection("users")
            val uid = firebaseAuth.currentUser?.uid
            val userDocument = usersCollection.document(uid?:"")

            userDocument.get().addOnSuccessListener {documentSnapshot->
                if(documentSnapshot.exists()){
                    val name = documentSnapshot.getString("name")
                    val profilePicture = documentSnapshot.getString("profilePicture")
                    setUsernameAndProfilePicture(name, profilePicture)
                }
            }
        }
    }
    fun updateUserRoutineFromFireStore(){
        viewModelScope.launch {
            val uid = firebaseAuth.currentUser?.uid
            val routinesCollectionRef = firestore.collection("users").document(uid?:"").collection("routines")
            routinesCollectionRef.get()
                .addOnSuccessListener { querySnapshot ->
                    val routinesList = mutableListOf<RoutineData>()

                    for(document in querySnapshot) {
                        val routineName = document.getString("routineName")
                        Log.d("firebase","routineName is  = $routineName")

                        val routineData = document.get("routine") as List<Map<String,Any>>
                        Log.d("firebase","routineData size = ${routineData.size}")

                        val exerciseList = mutableListOf<ExerciseWithSet>()
                        Log.d("firebase","exerciseList size = ${exerciseList.size}")

                        for (exerciseMap in routineData) {
                            val exerciseSetData = exerciseMap["exerciseSet"] as Map<String, Any>
                            val sets = exerciseSetData["sets"] as String
                            Log.d("firebase","sets = $sets")

                            val reps = exerciseSetData["reps"] as String
                            Log.d("firebase","reps = $reps")

                            val id = exerciseMap["id"] as String
                            Log.d("firebase","id = $id")

                            val exercise = ExerciseWithSet(id = id, ExerciseSet(sets = sets, reps = reps))
                            exerciseList.add(exercise)
                        }

                        val routine = RoutineData(routineName = routineName ?: "", routine = exerciseList)
                        routinesList.add(routine)
                        Log.d("firebase","routineslist size = ${routinesList.size}")
                    }
                    updateFirebaseRoutineList(routinesList)
                }

        }
    }
    fun checkForDuplicateRoutine(name: String): Boolean{
        return _firebaseRoutine.value.any { it.routineName == name }
    }
    fun updateFirebaseRoutineList(routines: List<RoutineData>){
        _firebaseRoutine.value = routines
        Log.d("firebase","routines add to firebaseRoutine = ${_firebaseRoutine.value.size}")
    }
    fun addRoutineToFireStore(routineName: String,routineList: List<ExerciseWithSet> = routine.value){
        val routineData = RoutineData(routineName = routineName,routineList)
        val isDuplicate = checkForDuplicateRoutine(routineName)
        if(!isDuplicate){
            viewModelScope.launch {
                try {
                    val uid = firebaseAuth.currentUser?.uid
                    val routineDocument = firestore.collection("users").document(uid?: "").collection("routines").document()
                    routineDocument.set(routineData)
                        .addOnSuccessListener {
                            Log.d("firestore", "Routine data added to Firestore.")
                        }
                        .addOnFailureListener { e ->
                            Log.d("firestore", "Error adding routine data to Firestore: $e")
                        }
                }catch (e:Exception){
                    Log.e("firebase","$e")
                }

            }
        }
        updateUserRoutineFromFireStore()
    }
    //update workout to firestore
    fun updateWorkoutToFireStore(routineName:String,duration: String, exercises: String, sets: String,date: String){
        val workout = Workout(routineName,duration,exercises,sets,date)
        viewModelScope.launch {
            val uid = firebaseAuth.currentUser?.uid
            val userDocument = firestore.collection("users").document(uid?:"")

            userDocument.update("workouts",FieldValue.arrayUnion(workout))
                .addOnSuccessListener {
                    Log.d("firebase","workout successfully added")
                    getWorkoutListFromFireStore()
                }
                .addOnFailureListener { e ->
                    Log.d("firebase","error: $e")
                }
        }
    }

    //fetching workout data from firestoree
    fun getWorkoutListFromFireStore(){
        val uid = firebaseAuth.currentUser?.uid
        val userDocument = firestore.collection("users").document(uid?:"")
        userDocument.get().addOnSuccessListener { dataSnapshot ->
            if(dataSnapshot.exists()){
                val workouts = mutableListOf<Workout>()

                val workoutsData = dataSnapshot.get("workouts") as List<HashMap<String,Any>>?
                var t_exercise = 0
                var t_sets = 0
                workoutsData?.forEach { workoutData ->
                    val routineName = workoutData["routineName"] as String
                    val duration = workoutData["duration"] as String
                    val exercises = workoutData["exercises"] as String
                    val sets = workoutData["sets"] as String
                    val date = workoutData["date"] as String
                    t_exercise += exercises.toInt()
                    t_sets += sets.toInt()
                    val workout = Workout(routineName,duration,exercises,sets,date)
                    workouts.add(workout)
                }
                _workoutList.value = workouts.reversed()
                _totalWorkouts.value = _workoutList.value.size.toString()
                _totalExercises.value = t_exercise.toString()
                _totalSets.value = t_sets.toString()
                
            }
        }
            .addOnFailureListener {
                Log.d("firebase","failed to fetch workout list from firestore")
            }
    }
    private fun setUsernameAndProfilePicture(name: String?,profilePicture: String?){
        _profileUiState.value = ProfileUiState(name,profilePicture)
        Log.d("firebase","username and profile picture is stored successfully ${profileUiState.value.username} ${profileUiState.value.profileUrl}")
    }
    fun clearCurrentExerciseIds(){
        exerciseIds.value.clear()
    }


    fun addExerciseIdToList(id : String){
        if(!exerciseIds.value.contains(id)){
            exerciseIds.value.add(id)
            exerciseIds.value = exerciseIds.value.toMutableList()
        }
    }
    fun removeExerciseIdFromList(id: String){
        exerciseIds.value.remove(id)
        exerciseIds.value = exerciseIds.value.toMutableList()
    }
    fun addSetToRoutine(id: String,sets: String, reps : String){
        Log.d("appviewmodel", "id = $id  size = ${routine.value.size}")
        val exerciseWithSet = ExerciseWithSet(id = id, exerciseSet = ExerciseSet(sets = sets, reps = reps))
        if(!routine.value.contains(exerciseWithSet)){
            routine.value.add(exerciseWithSet)
            Log.d("appviewmodel", "id = $id  size = ${routine.value.size}")
        }

    }

    fun addExercisesToRoutineExercises(){
        viewModelScope.launch {
            exerciseIds.value.forEach {
                val current = repository.getExerciseById(it)
                if(!_routineExercises.value.contains(current)){
                    _routineExercises.value.add(current)
                    Log.d("appviewmodel","routineExercises size = ${_routineExercises.value.size} ")
                }
            }
        }

    }
    fun clearRoutineExercises(){
        _routineExercises.value.clear()
        routine.value.clear()
    }

    fun removeExerciseFromRoutineExercise(exercise: ExercisesItem){
        viewModelScope.launch {
            _routineExercises.value.remove(exercise)
            Log.d("appviewmodel","routineExercise = ${_routineExercises.value.size} ")
        }

    }

    fun updateRoutineTitle(name: String){
        _routineTitle.value = name
    }


    @RequiresApi(Build.VERSION_CODES.S)
    fun startTimer(){
        timer = fixedRateTimer(initialDelay = 1000L, period = 1000L){
            time = time.plus(Duration.ofSeconds(1L))
            updateTimeState()
        }
        isPlaying = true
        Log.d("timer","timer started")
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

    fun updateStartRoutineList(routineName: String){
        _isStartRoutineListUpdated.value = false
        val startRoutine = _firebaseRoutine.value.find { it.routineName == routineName }
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


    fun resetStartRoutineList(){
        _startRoutineList.value.clear()
        Log.d("appviewmodel","_startroutinList size = ${_startRoutineList.value.size}")
    }

}
