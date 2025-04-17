package com.example.whatsapp.presentation.WelcomeScreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.whatsapp.R
import com.example.whatsapp.presentation.authscreen.AuthNavItem


@Composable
fun WelcomeScreen(navController: NavController) {

    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color.Gray)) {
            append("Read our ")
        }

        pushStringAnnotation(tag = "PRIVACY", annotation = "privacy_policy")
        withStyle(style = SpanStyle(color = colorResource(R.color.light_green))) {
            append("Privacy Policy ")
        }
        pop()

        withStyle(style = SpanStyle(color = Color.Gray)) {
            append("and tap 'Agree and Continue' to accept the ")
        }

        pushStringAnnotation(tag = "TERMS", annotation = "terms_of_service")
        withStyle(style = SpanStyle(color = colorResource(R.color.light_green))) {
            append("Terms of Service")
        }
        pop()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(R.drawable.whatsapp_sticker),
            contentDescription = null,
            modifier = Modifier.size(240.dp)
        )

        Text(text = "Welcome to WhatsApp", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(24.dp))

        ClickableText(
            text = annotatedString,
            style = TextStyle(
                fontSize = 14.sp,
                lineHeight = 20.sp,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
            onClick = { offset ->
                annotatedString.getStringAnnotations(start = offset, end = offset).firstOrNull()
                    ?.let { annotation ->
                        when (annotation.tag) {
                            "PRIVACY" -> {
                                // Handle Privacy Policy click
                                Log.d("ClickableText", "Privacy Policy clicked")
                            }

                            "TERMS" -> {
                                // Handle Terms of Service click
                                Log.d("ClickableText", "Terms of Service clicked")
                            }
                        }
                    }
            }
        )
        Spacer(modifier = Modifier.height(24.dp))


        Button(
            onClick = {navController.navigate(AuthNavItem.userRegisterationScreen.route){
                popUpTo(AuthNavItem.WelcomeScreen.route) { inclusive = true } // remove splash from backstack
            } },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.dark_green))
        ) {
            Text(text = "Agree And Continues", color = Color.White, fontSize = 16.sp)

        }


    }

}

