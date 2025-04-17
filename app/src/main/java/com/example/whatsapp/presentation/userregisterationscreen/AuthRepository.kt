package com.example.whatsapp.presentation.userregisterationscreen

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineStart
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.io.encoding.Base64

class AuthRepository @Inject constructor(val auth: FirebaseAuth, val database: FirebaseDatabase) {
    val userref = database.getReference().child("USERS")
    fun startPhoneAuth(
        phoneNumber: String,
        activity: Activity,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks,
    ) {
        val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyWithCredential(
        phoneNumber: String,
        credential: PhoneAuthCredential,
        onSuccess: (FirebaseUser?) -> Unit,
        onFailure: (String) -> Unit,
    ) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val firebaseUser = auth.currentUser
                    val userId = firebaseUser?.uid ?: return@addOnCompleteListener

                    val user = UserModel(
                        userId = userId,
                        phoneNUmber = phoneNumber,
                        status = "Hey there! I am using WhatsApp"
                    )

                    // ✅ Save basic info to Firebase
                    userref.child(userId).get()
                        .addOnSuccessListener { snapshot ->
                            if (!snapshot.exists()) {
                                // 🔁 Only create if not exists
                                userref.child(userId).setValue(user)
                                    .addOnSuccessListener {
                                        onSuccess(firebaseUser)
                                    }
                                    .addOnFailureListener { error ->
                                        onFailure(error.message ?: "Failed to save user")
                                    }
                            } else {
                                // ✅ User exists, skip update
                                onSuccess(firebaseUser)
                            }
                        }
                        .addOnFailureListener { error ->
                            onFailure(error.message ?: "Failed to check user existence")
                        }

                } else {
                    onFailure(task.exception?.message ?: "Something went wrong")
                }
            }
    }


    fun saveUserProfileLocal(
        name: String,
        status: String,
        imageUri: String,
        onSuccess: () -> Unit,
        onError: () -> Unit,
    ) {
        val userId = auth.currentUser?.uid ?: return onError()


        val updates = mapOf(
            "name" to name,
            "status" to status,
            "profileimage" to imageUri,
            "isProfileComplete" to true
        )

        userref.child(userId).updateChildren(updates)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError() }

    }


    fun getUserDetailsFromFirebase(
        onSuccess: (UserModel) -> Unit,
        onError: (String) -> Unit,
    ) {
        val userId = auth.currentUser?.uid ?: return
        userref.child(userId).get().addOnSuccessListener { snapshot ->
            val user = snapshot.getValue(UserModel::class.java)
            if (user != null) {
                onSuccess(user)
            } else {
                onError("User data not found")
            }
        }.addOnFailureListener {
            onError(it.message ?: "Failed to fetch data")
        }
    }

}
