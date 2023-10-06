package com.vishalsingh444888.sweatsync.ui.viewmodel

import android.util.Log
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
import javax.inject.Inject

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
    init{
        viewModelScope.launch {
            storeDataInDatabase()
        }
        _routineExercises.value.clear()
    }
    fun fetchData(){
        viewModelScope.launch {
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
    fun addRoutineToFireStore(routineName: String){
        val routineData = RoutineData(routineName = routineName,routine.value)
        viewModelScope.launch {
            val uid = firebaseAuth.currentUser?.uid
            val routineDocument = firestore.collection("users").document(uid?: "").collection("routines").document()
            routineDocument.set(routineData)
                .addOnSuccessListener {
                    Log.d("firestore", "Routine data added to Firestore.")
                }
                .addOnFailureListener { e ->
                    Log.d("firestore", "Error adding routine data to Firestore: $e")
                }
        }
        updateUserRoutineFromFireStore()
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

}
