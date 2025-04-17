@file:Suppress("DEPRECATION")

package com.example.whatsapp.presentation.userregisterationscreen

import android.app.Activity
import android.util.Log
import android.util.Log.*
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.whatsapp.MainActivity
import com.example.whatsapp.R
import com.example.whatsapp.presentation.authscreen.AuthNavItem
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun userRegisterationScreen(
    navController: NavController,
    locationViewModel: LocationViewModel,
    authViewModel: AuthViewModel,
) {

    val context = LocalContext.current

    val countryOptions by locationViewModel.countryList.collectAsState()

    val selectedCountry by locationViewModel.selectedCountry.collectAsState()

    val authstate by authViewModel.authState.collectAsState()

    var phoneNumber by remember { mutableStateOf("") }

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


    LaunchedEffect(authstate) {
        if (authstate is AuthState.CodeSent) {
            // ✅ Navigate to MainActivity or next screen
            navController.navigate(AuthNavItem.OtpVerificationScreen.route) {
                popUpTo(AuthNavItem.userRegisterationScreen.route) { inclusive = true }
            }
        }

        if (authstate is AuthState.Loading) {

        }

    }


    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Enter your phone number ",
            color = colorResource(R.color.dark_green),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))
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
                        d("myNumber", "")
                    }

                }

            }

        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp)) {

            SimpleSpinner(
                options = countryOptions,
                selectedOption = selectedCountry,
                onOptionSelected = { locationViewModel.selectCountry(it) }
            )
        }


        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            // Country Code TextField
            TextField(
                readOnly = true,
                value = selectedCountry?.phonecode?.let { "+$it" } ?: "",
                onValueChange = {},
                modifier = Modifier
                    .weight(1f).height(56.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    unfocusedIndicatorColor = colorResource(R.color.light_green),
                    focusedIndicatorColor = colorResource(R.color.light_green),
                ),
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.width(8.dp))
            // Phone Number TextField
            TextField(
                value = phoneNumber, // You can use remember state here
                onValueChange = { phoneNumber = it },
                modifier = Modifier.weight(3f).height(56.dp),
                placeholder = {
                    Text("Phone number", fontSize = 20.sp)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = colorResource(R.color.light_green),
                    unfocusedIndicatorColor = colorResource(R.color.light_green)
                ),
                textStyle = TextStyle(
                    fontSize = 20.sp,
                    textAlign = TextAlign.Start
                ),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Carrier charges may apply", color = Color.Gray,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(26.dp))

        TextButton(
            onClick = {
                if (selectedCountry == null) {
                    Toast.makeText(context, "Please select a country", Toast.LENGTH_SHORT).show()
                    return@TextButton
                }
                val isPhoneValid = phoneNumber.isNotBlank() && phoneNumber.all { it.isDigit() }

                if (!isPhoneValid) {
                    Toast.makeText(context, "Please enter a valid phone number", Toast.LENGTH_SHORT)
                        .show()
                    return@TextButton
                }

                d("Register", "Country: ${selectedCountry!!.name}, Phone: $phoneNumber")

                val phonenumber = selectedCountry?.phonecode?.let { "+$it$phoneNumber" }
                val activity = context as Activity

                if (phonenumber != null) {
                    authViewModel.setPhoneNumber(phonenumber)
                    authViewModel.startPhoneAuth(activity)
                }


            },
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.dark_green))
        ) {
            Text(text = "Send OTP", color = Color.White, fontSize = 16.sp)
        }


    }
    when (authstate) {
        is AuthState.CodeSent -> {
        }

        is AuthState.Success -> {
            Toast.makeText(
                context,
                "Login successful: ${(authstate as AuthState.Success).phone}",
                Toast.LENGTH_SHORT
            ).show()

        }

        is AuthState.Error -> {
            Toast.makeText(
                context,
                "Error: ${(authstate as AuthState.Error).message}",
                Toast.LENGTH_SHORT
            ).show()
            Log.d("firebase error", "Error: ${(authstate as AuthState.Error).message}")
        }

        else -> {}
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleSpinner(
    options: List<Country>,
    selectedOption: Country?,
    onOptionSelected: (Country) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var isFirstTime by remember { mutableStateOf(true) } // ✅ Track first time


    LaunchedEffect(expanded) {
        if (expanded) {
            showDialog = true
            delay(if (isFirstTime) 2000 else 500) // ✅ Conditional delay
            showDialog = false
            isFirstTime = false
        }
    }

    if (showDialog) {
        LottieLoadingDialog()
    }


    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selectedOption?.name ?: "Select Country",
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Icon",
                    tint = colorResource(R.color.light_green),
                    modifier = Modifier.rotate(if (expanded) 180f else 0f)

                )
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor = colorResource(R.color.light_green),
                unfocusedIndicatorColor = colorResource(R.color.light_green),
            ),
            textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 20.sp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { country ->
                DropdownMenuItem(
                    text = { Text(country.name) },
                    onClick = {
                        onOptionSelected(country)
                        expanded = false
                    }
                )
            }
        }
    }
}


@Composable
fun LottieLoadingDialog() {
    Dialog(onDismissRequest = {}) {
        Box(
            modifier = Modifier
                .size(150.dp)
                .background(Color.Transparent, shape = RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
            val progress by animateLottieCompositionAsState(composition)

            LottieAnimation(
                composition = composition,
                progress = progress,
                modifier = Modifier.size(150.dp)
            )
        }
    }
}
