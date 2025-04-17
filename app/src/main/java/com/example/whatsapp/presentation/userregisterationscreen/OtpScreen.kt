@file:Suppress("DEPRECATION")

package com.example.whatsapp.presentation.userregisterationscreen

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.whatsapp.MainActivity
import com.example.whatsapp.R
import com.example.whatsapp.presentation.authscreen.AuthNavItem


@Composable
fun OtpVerificationScreen(
    navController: NavHostController,
    locationViewModel: LocationViewModel,
    authViewModel: AuthViewModel,
) {
    val context = LocalContext.current

    val authstate by authViewModel.authState.collectAsState()
    // State for OTP input
    var otp by remember { mutableStateOf("") }

    val countryCode = locationViewModel.selectedCountry//

    LaunchedEffect(authstate) {
        if (authstate is AuthState.Success) {
            // ✅ Navigate to MainActivity or next screen
            navController.navigate(AuthNavItem.UserProfileScreen.route) {
                popUpTo(AuthNavItem.OtpVerificationScreen.route) { inclusive = true }
            } // optional: remove OTP screen from backstack
        }
    }


    val annotedstring = buildAnnotatedString {

        withStyle(style = SpanStyle(color = Color.Gray)) {
            append("WhatsApp will need to verify yout phone number.")
        }

        pushStringAnnotation("myNumber", annotation = "my_number")
        withStyle(style = SpanStyle(color = colorResource(R.color.light_green))) {
            append("Whats's my number?")
        }
        pop()

    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Heading
        Text(
            text = "Verify your phone number",
            color = colorResource(R.color.dark_green),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        // Phone Number with Country Code

        ClickableText(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
            text = annotedstring,
            style = TextStyle(
                fontSize = 14.sp,
                lineHeight = 20.sp,
                textAlign = TextAlign.Center
            ),
            onClick = {
                annotedstring.getStringAnnotations(start = it, end = it).firstOrNull()?.let {


                    if (it.tag == "myNumber") {
                        Log.d("myNumber", "")
                    }

                }

            }

        )
        Spacer(modifier = Modifier.height(24.dp))


        Text(text = "Enter OTP", fontSize = 16.sp, color = colorResource(R.color.light_green))

        // OTP Input Field
        OutlinedTextField(
            value = otp,
            onValueChange = { otp = it },
            placeholder = { Text(text = "OTP") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Verify OTP Button
        Button(
            onClick = {
                authViewModel.verifyOtp(otp)
            }, colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.dark_green)
            )
        ) {
            Text(text = "Verify OTP")
        }
    }
}
