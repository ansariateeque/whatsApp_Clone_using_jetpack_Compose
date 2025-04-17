package com.example.whatsapp.presentation.userregisterationscreen

sealed class AuthState {
    object Idle : AuthState()
    object Loading:AuthState()
    object CodeSent : AuthState()
    data class Success(val phone: String?) : AuthState()
    data class Error(val message: String) : AuthState()
}
