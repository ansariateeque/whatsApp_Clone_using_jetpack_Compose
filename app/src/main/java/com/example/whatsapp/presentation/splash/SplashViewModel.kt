package com.example.whatsapp.presentation.splash

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(val auth: FirebaseAuth, val database: FirebaseDatabase) :
    ViewModel() {

    suspend fun checkUserAndNavigate(
        context: Context,
        onNavigateToWelcome: () -> Unit,
        onNavigateToUserProfile: () -> Unit,
        onNavigateToMainActivity: () -> Unit,
    ) {
        val user = auth.currentUser
        if (user == null) {
            onNavigateToWelcome()
        } else {
            val uid = user.uid
            val userRef = database.getReference("USERS").child(uid)
            try {
                val snapshot = userRef.get().await()
                val isProfileComplete =
                    snapshot.child("isProfileComplete").getValue(Boolean::class.java) ?: false
                if (isProfileComplete) {
                    onNavigateToMainActivity()
                } else {
                    onNavigateToUserProfile()
                }
            } catch (e: Exception) {
                onNavigateToUserProfile()
            }
        }
    }
}