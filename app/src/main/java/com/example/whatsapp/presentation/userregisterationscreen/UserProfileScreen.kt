package com.example.whatsapp.presentation.userregisterationscreen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.whatsapp.MainActivity
import com.example.whatsapp.presentation.mainscreen.base64ToBitmap
import com.example.whatsapp.presentation.mainscreen.uriToBase64
import java.io.ByteArrayOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    navController: NavController,
    viewModel: AuthViewModel,
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.fetchUserProfile()
    }

    val user by viewModel.userModel

    // Local states using remember
    var name by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }

    var profileImageBitmap by remember { mutableStateOf<Bitmap?>(null) }


    val base64Image = profileImageUri?.let { uriToBase64(context, it) } ?: ""


    LaunchedEffect(user) {
        user?.let {
            name = it.name
            status = it.status
            if (it.profileimage.isNotBlank()) {
                profileImageBitmap = base64ToBitmap(it.profileimage)
            }
        }
    }

    // Image picker launcher
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            profileImageUri = it
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Profile Image Box
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
                .clickable { launcher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            when {
                profileImageUri != null -> {
                    // Local image (from gallery)
                    AsyncImage(
                        model = profileImageUri,
                        contentDescription = "Profile Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                user?.profileimage?.isNotBlank() == true -> {
                    // Firebase image (base64)
                    val bitmap = base64ToBitmap(user!!.profileimage)
                    bitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "Profile Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                else -> {
                    // Default icon
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Default Profile",
                        tint = Color.White,
                        modifier = Modifier.size(60.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Name input
        TextField(
            value = name,
            onValueChange = { name = it },
            placeholder = {
                Text("Name", fontSize = 20.sp)
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Status input
        TextField(
            value = status,
            onValueChange = { status = it },
            placeholder = {
                Text("status", fontSize = 20.sp)
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Save Button
        Button(
            onClick = {
                if (name.isNotBlank()) {
                    viewModel.saveUserProfileLocal(
                        name = name,
                        status = status,
                        base64image = base64Image,
                        onSuccess = {
                            Toast.makeText(context, "Profile Saved", Toast.LENGTH_SHORT).show()
                            context.startActivity(Intent(context, MainActivity::class.java))
                            (context as? Activity)?.finish()
                        },
                        onError = {
                            Toast.makeText(context, "Failed to save", Toast.LENGTH_SHORT).show()
                        }
                    )
                } else {
                    Toast.makeText(context, "Name is required", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Save", fontSize = 18.sp)
        }
    }
}


