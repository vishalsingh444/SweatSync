package com.vishalsingh444888.sweatsync.ui.auth

data class AuthState(
    val isLoading: Boolean = false,
    val isSuccess: String? = "",
    val isError: String? = ""
)
