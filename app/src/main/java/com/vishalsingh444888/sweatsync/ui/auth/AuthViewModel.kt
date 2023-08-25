package com.vishalsingh444888.sweatsync.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.vishalsingh444888.sweatsync.PreferenceManager
import com.vishalsingh444888.sweatsync.repository.AuthRepository
import com.vishalsingh444888.sweatsync.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val googleSignInClient: GoogleSignInClient,
    private val preferenceManager: PreferenceManager,
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
) : ViewModel() {

    private val _signUpState = Channel<AuthState>()
    val signUpState = _signUpState.receiveAsFlow()

    private val _signInState = Channel<AuthState>()
    val signInState = _signInState.receiveAsFlow()

    private val _googleSignInState = MutableStateFlow(GoogleSignInState())
    val googleSignInState: StateFlow<GoogleSignInState> = _googleSignInState

    private val _authenticated = MutableStateFlow(preferenceManager.isAuthenticated)
    val authenticated: StateFlow<Boolean> = _authenticated

    private fun onSignInSuccess() {
        preferenceManager.isAuthenticated = true
    }

    private fun onSignOut() {
        preferenceManager.isAuthenticated = false
    }

    fun signOut() {
        viewModelScope.launch {
            onSignOut()
            repository.signOut()
            googleSignInClient.signOut()
        }
    }

    fun googleSignIn(credential: AuthCredential) = viewModelScope.launch {
        repository.googleSignIn(credential).collect { result ->
            when (result) {
                is Resource.Error -> {
                    _googleSignInState.value = GoogleSignInState(isError = result.message)
                }

                is Resource.Loading -> {
                    _googleSignInState.value = GoogleSignInState(isLoading = true)
                }

                is Resource.Success -> {
                    _googleSignInState.value = GoogleSignInState(isSuccess = result.data)
                }
            }
        }
        onSignInSuccess()
    }


    fun registerUser(email: String, password: String) = viewModelScope.launch {
        repository.registerUser(email, password).collect { result ->
            when (result) {
                is Resource.Error -> {
                    _signUpState.send(AuthState(isError = result.message))
                }

                is Resource.Loading -> {
                    _signUpState.send(AuthState(isLoading = true))
                }

                is Resource.Success -> {
                    _signUpState.send(AuthState(isSuccess = "sign Up successful"))
                }
            }
        }
        onSignInSuccess()
    }

    fun loginUser(email: String, password: String) = viewModelScope.launch {
        repository.loginUser(email, password).collect { result ->
            when (result) {
                is Resource.Error -> {
                    _signInState.send(AuthState(isError = result.message))
                }

                is Resource.Loading -> {
                    _signInState.send(AuthState(isLoading = true))
                }

                is Resource.Success -> {
                    _signInState.send(AuthState(isSuccess = "Sign In Successful"))
                }
            }
        }
        onSignInSuccess()
    }

    fun updateUserDetailsToFireStore(displayName: String?, profilePicture: String?) {
        val user = firebaseAuth.currentUser
        val uid = user?.uid
        Log.d("Firestore","uid: $uid")
        val userData = mutableMapOf(
            "name" to displayName,
            "profilePicture" to profilePicture
        )
        firebaseFirestore.collection("users").document(uid?:"").set(userData)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Firestore", "User data storage successful")
                } else {
                    Log.e("Firestore", "User data storage failed", task.exception)
                }
            }
    }

}