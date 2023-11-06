package com.vishalsingh444888.sweatsync.ui.screens.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.vishalsingh444888.sweatsync.ui.viewmodel.ProfileUiState
import com.vishalsingh444888.sweatsync.ui.viewmodel.Workout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
): ViewModel() {

    private val _profileUiState = MutableStateFlow(ProfileUiState())
    val profileUiState = _profileUiState

    private val _workoutList = MutableStateFlow<List<Workout>>(listOf())
    val workoutList = _workoutList.asStateFlow()

    private val _totalWorkouts = MutableStateFlow<String>("")
    val totalWorkouts = _totalWorkouts.asStateFlow()

    private val _totalExercises = MutableStateFlow("")
    val totalExercises = _totalExercises.asStateFlow()

    private val _totalSets = MutableStateFlow("")
    val totalSets = _totalSets.asStateFlow()

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

    private fun setUsernameAndProfilePicture(name: String?,profilePicture: String?){
        _profileUiState.value = ProfileUiState(name,profilePicture)
        Log.d("firebase","username and profile picture is stored successfully ${profileUiState.value.username} ${profileUiState.value.profileUrl}")
    }

    fun getWorkoutListFromFireStore(){
        val uid = firebaseAuth.currentUser?.uid
        val userDocument = firestore.collection("users").document(uid?:"")
        userDocument.get()
            .addOnSuccessListener { dataSnapshot ->
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


}