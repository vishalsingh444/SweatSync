package com.vishalsingh444888.sweatsync.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vishalsingh444888.sweatsync.data.model.Exercises
import com.vishalsingh444888.sweatsync.data.model.ExercisesItem
import com.vishalsingh444888.sweatsync.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> get() = _uiState
    var data = Exercises()
    init{
        fetchData()
    }
    private fun fetchData(){
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try{
                val result = repository.getAllExercise()
                data = result
                _uiState.value = UiState.Success(result,result[0])
            }
            catch(e: Exception){
                _uiState.value = UiState.Error(e.message)
            }
        }
    }
    fun updateCurrentExercise(exercisesItem: ExercisesItem){
        _uiState.value = UiState.Success(data,exercisesItem)
    }
}