package com.vishalsingh444888.sweatsync.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.vishalsingh444888.sweatsync.data.model.Exercises
import com.vishalsingh444888.sweatsync.data.model.ExercisesItem
import com.vishalsingh444888.sweatsync.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.internal.wait
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
    init{
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
    private fun setUsernameAndProfilePicture(name: String?,profilePicture: String?){
        _profileUiState.value = ProfileUiState(name,profilePicture)
        Log.d("firebase","username and profile picture is stored successfully ${profileUiState.value.username} ${profileUiState.value.profileUrl}")
    }
}
