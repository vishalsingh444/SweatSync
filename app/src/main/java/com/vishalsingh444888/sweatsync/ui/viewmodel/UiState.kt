package com.vishalsingh444888.sweatsync.ui.viewmodel

import com.vishalsingh444888.sweatsync.data.model.Exercises

sealed class UiState{
    object Loading: UiState()
    data class Success(val exercises: Exercises): UiState()
    data class Error(val message:String?): UiState()
}