package com.example.whatsapp.presentation.userregisterationscreen

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(val repository: AuthRepository) :ViewModel() {

    private var _verificationId = ""
    var verificationId: String
        get() = _verificationId
        private set(value) {
            _verificationId = value
        }

    var authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authStateFlow: StateFlow<AuthState> = authState

    private var _phoneNumber: String = ""
    val phoneNumber: String
        get() = _phoneNumber


    private val _userModel = mutableStateOf<UserModel?>(null)
    val userModel: State<UserModel?> = _userModel

    fun fetchUserProfile() {
        repository.getUserDetailsFromFirebase(
            onSuccess = { user ->
                _userModel.value = user
            },
            onError = {
                // Handle error if needed
            }
        )
    }

    fun setPhoneNumber(number: String) {
        _phoneNumber = number.trim()
    }

    fun startPhoneAuth(activity: Activity) {
        if (_phoneNumber.isBlank()) {
            authState.value = AuthState.Error("Phone number is empty")
            return
        }

        authState.value=AuthState.Loading
        repository.startPhoneAuth(
            _phoneNumber,
            activity,
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    repository.verifyWithCredential(
                        phoneNumber,
                        credential,
                        onSuccess = { user ->
                            authState.value = AuthState.Success(user?.phoneNumber)
                        },
                        onFailure = { error ->
                            authState.value = AuthState.Error(error)
                        }
                    )
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    authState.value = AuthState.Error(e.localizedMessage ?: "Verification failed")
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    this@AuthViewModel.verificationId = verificationId
                    authState.value = AuthState.CodeSent
                }
            })

    }

    fun verifyOtp(otp: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, otp)
        repository.verifyWithCredential(
            _phoneNumber,
            credential,
            onSuccess = { user ->
                authState.value = AuthState.Success(user?.phoneNumber)
            },
            onFailure = { error ->
                authState.value = AuthState.Error(error)
            }
        )
    }

    fun saveUserProfileLocal(
        name: String,
        status: String,
        base64image: String,
        onSuccess: () -> Unit,
        onError: () -> Unit,
    ) {


        repository.saveUserProfileLocal(name,status,base64image,onSuccess,onError)
    }

}