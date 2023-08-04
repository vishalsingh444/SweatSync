package com.vishalsingh444888.sweatsync.ui.auth

import com.google.firebase.auth.AuthResult

data class GoogleSignInState(
    val isSuccess: AuthResult? = null,
    val isLoading: Boolean = false,
    val isError: String? = ""
)
