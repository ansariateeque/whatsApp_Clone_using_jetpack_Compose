package com.example.whatsapp.presentation.authscreen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.whatsapp.presentation.WelcomeScreen.WelcomeScreen
import com.example.whatsapp.presentation.splash.SplashScreen
import com.example.whatsapp.presentation.userregisterationscreen.AuthViewModel
import com.example.whatsapp.presentation.userregisterationscreen.LocationViewModel
import com.example.whatsapp.presentation.userregisterationscreen.OtpVerificationScreen
import com.example.whatsapp.presentation.userregisterationscreen.UserProfileScreen
import com.example.whatsapp.presentation.userregisterationscreen.userRegisterationScreen


@Composable
fun AuthNavigationGraph(navController: NavHostController) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val locationViewModel: LocationViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = AuthNavItem.SplashScreen.route) {

        composable(AuthNavItem.SplashScreen.route) { SplashScreen(navController=navController) }
        composable(AuthNavItem.WelcomeScreen.route) { WelcomeScreen(navController) }

        composable(AuthNavItem.userRegisterationScreen.route) {
            userRegisterationScreen(
                navController, locationViewModel = locationViewModel, authViewModel = authViewModel
            )
        }

        composable(AuthNavItem.OtpVerificationScreen.route) {
            OtpVerificationScreen(
                navController, locationViewModel = locationViewModel, authViewModel = authViewModel
            )
        }

        composable(AuthNavItem.UserProfileScreen.route) {
            UserProfileScreen(navController, authViewModel)
        }

    }

}