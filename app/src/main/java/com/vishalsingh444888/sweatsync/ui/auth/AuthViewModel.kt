package com.vishalsingh444888.sweatsync.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel: ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authenticated = MutableStateFlow(false)
    val authenticated : StateFlow<Boolean> = _authenticated

    private fun onSignInSuccess(){
        _authenticated.value = true
    }
    private fun onSignOut(){
        _authenticated.value = false
    }
    fun signInWithEmailAndPassword(email: String,password: String,callBack: (Boolean) -> Unit){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{task ->
                if(task.isSuccessful){
                    onSignInSuccess()
                    callBack(true)
                }else{
                    callBack(false)
                    val exception = task.exception
                    if (exception is FirebaseAuthInvalidCredentialsException) {
                        val errorCode = exception.errorCode
                        when (errorCode) {
                            "ERROR_INVALID_EMAIL" -> {
                                // Handle invalid email format
                            }

                            "ERROR_WRONG_PASSWORD" -> {
                                // Handle incorrect password
                            }
                            // Handle other specific error codes as needed
                            else -> {
                                // Handle generic sign-in failure
                            }
                        }
                    }
                    else {
                        // Log the error message without showing it to the user
                        Log.e("AuthViewModel", "Sign-in error: ${exception?.message}")
                    }
                }
            }
    }

    fun registerWithEmailAndPassword(email: String,password: String,callBack: (Boolean) ->Unit){
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener { task->
                if(task.isSuccessful){
                    onSignInSuccess()
                    callBack(true)
                }else{
                    callBack(false)
                }
            }
    }
}