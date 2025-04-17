package com.example.whatsapp.presentation.authscreen

sealed class AuthNavItem(val route: String) {

    object SplashScreen : AuthNavItem("SplashScreen")
    object WelcomeScreen : AuthNavItem("WelcomeScreen")
    object userRegisterationScreen : AuthNavItem("userRegisterationScreen")
    object OtpVerificationScreen : AuthNavItem("OtpVerificationScreen")
    object UserProfileScreen : AuthNavItem("UserProfileScreen")
}