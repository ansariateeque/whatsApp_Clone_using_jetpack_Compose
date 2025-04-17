package com.example.whatsapp.presentation.splash

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.whatsapp.MainActivity
import com.example.whatsapp.R
import com.example.whatsapp.presentation.authscreen.AuthNavItem
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import javax.inject.Inject

@Composable
fun SplashScreen(splashViewModel: SplashViewModel= hiltViewModel(), navController: NavHostController) {

    val context = LocalContext.current


    LaunchedEffect(Unit) {
        delay(1500)
        splashViewModel.checkUserAndNavigate(
            context = context,
            onNavigateToWelcome = {
                navController.navigate(AuthNavItem.WelcomeScreen.route) {
                    popUpTo(AuthNavItem.SplashScreen.route) { inclusive = true }
                }
            },
            onNavigateToUserProfile = {
                navController.navigate(AuthNavItem.UserProfileScreen.route) {
                    popUpTo(AuthNavItem.SplashScreen.route) { inclusive = true }
                }
            },
            onNavigateToMainActivity = {
                context.startActivity(Intent(context, MainActivity::class.java))
                (context as? Activity)?.finish()
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(R.drawable.whatsapp_icon),
            contentDescription = null,
            modifier = Modifier.size(80.dp).align(Alignment.Center)
        )

        Column(modifier = Modifier.align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally) {

            Text(text = "From", fontSize = 18.sp, fontWeight = FontWeight.Bold)

            Row {
                Icon(
                    painter = painterResource(R.drawable.meta),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = colorResource(id = R.color.light_green) // ✅ working now
                )
                Spacer(modifier = Modifier.width(5.dp)) // Acts like vertical margin

                Text(
                    text = "Meta",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.light_green),
                )

            }

        }

    }

}