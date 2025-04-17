@file:Suppress("UNUSED_EXPRESSION")

package com.example.whatsapp.presentation.mainscreen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.whatsapp.R
import com.example.whatsapp.presentation.floatingactionbutton.UserListScreen
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit


@Composable
@Preview(showBackground = true, showSystemUi = true)
fun MainScreen() {

    val navController = rememberNavController()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route


    Scaffold(
        floatingActionButton = {
            when (currentRoute) {
                "Chats" -> {
                    MyFloatingActionButton(painter = painterResource(R.drawable.chat_icon)) {
                        navController.navigate(BottomNavItem.UserList.route)
                    }
                }

                "Updates" -> {
                    MyFloatingActionButton(painter = painterResource(R.drawable.baseline_photo_camera_24)) { }
                }

                "Communities" -> {
                }

                "Calls" -> {
                    MyFloatingActionButton(painter = painterResource(R.drawable.add_call)) { }
                }

            }
        },
        bottomBar = {
            if (BottomNavItem.Chats.route == currentRoute ||
                BottomNavItem.Updates.route == currentRoute ||
                BottomNavItem.Communities.route == currentRoute ||
                BottomNavItem.Calls.route == currentRoute
            ) {
                BottomNavigationBar(navController)
            }
        }
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(it)) {


            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                BottomNavigationGraph(navController)
            }

        }


    }


}


@Composable
fun topAppBar(
    currentRoute: String,
    color: Color,
    onCameraClick: (String) -> Unit,
    onSearchClick: (String) -> Unit,
    onMoreClick: (String) -> Unit,
    showMenu: Boolean = false,
    onDismissMenu: () -> Unit = {},
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = if (currentRoute == "Chats") "WhatsApp" else currentRoute,
            color = color,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp)
        )

        Row() {
            if (currentRoute != BottomNavItem.Communities.route) {
                IconButton(onClick = { onCameraClick(currentRoute) }) {
                    Icon(
                        painter = painterResource(R.drawable.camera),
                        contentDescription = "",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            IconButton(onClick = { onSearchClick(currentRoute) }) {
                Icon(
                    painter = painterResource(R.drawable.search),
                    contentDescription = "",
                    modifier = Modifier.size(24.dp)
                )
            }
            Box {
                IconButton(onClick = { onMoreClick(currentRoute) }) {
                    Icon(
                        painter = painterResource(R.drawable.more),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }

                when (currentRoute) {
                    BottomNavItem.Updates.route -> {
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { onDismissMenu() }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Status Privacy") },
                                onClick = {
                                    onDismissMenu()
                                    // Handle click
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Create Channel") },
                                onClick = {
                                    onDismissMenu()
                                    // Handle click
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Settings") },
                                onClick = {
                                    onDismissMenu()
                                    // Handle click
                                }
                            )
                        }
                    }
                }

            }
        }


    }
    HorizontalDivider()
}


@Composable
fun MyFloatingActionButton(
    painter: Painter,
    onClick: () -> Unit,
) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = colorResource(R.color.light_green),
        contentColor = Color.White,
        modifier = Modifier.size(65.dp)


    ) {
        Icon(
            painter = painter,
            contentDescription = "FAB Icon",
            modifier = Modifier.size(28.dp)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClose: () -> Unit,
    onSearchSubmit: () -> Unit = {},
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            painter = painterResource(id = R.drawable.search),
            contentDescription = "Search",
            modifier = Modifier.size(24.dp)
        )

        androidx.compose.material3.TextField(
            value = query,
            onValueChange = { onQueryChange(it) },
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp).focusRequester(focusRequester),
            placeholder = { Text("Search...") },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White.copy(alpha = 0.5f), // <- transparent white
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchSubmit() // 🔥 Trigger when search is submitted
                }
            )
        )

        IconButton(onClick = onClose) {
            Icon(
                painter = painterResource(id = R.drawable.cross), // Add a cross icon
                contentDescription = "Close",
                modifier = Modifier.size(24.dp)
            )
        }
    }

    HorizontalDivider()
}

fun formatChatTimestamp(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp

    val calendar = Calendar.getInstance().apply {
        timeInMillis = timestamp
    }

    return when {
        TimeUnit.MILLISECONDS.toHours(diff) < 24 -> {
            // Same day - show time like "12:45 PM"
            val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
            sdf.format(Date(timestamp))
        }

        TimeUnit.MILLISECONDS.toHours(diff) in 24..47 -> {
            // Yesterday
            "Yesterday"
        }

        else -> {
            // More than 2 days ago - show date like "Mar 21"
            val sdf = SimpleDateFormat("dd MMM", Locale.getDefault())
            sdf.format(Date(timestamp))
        }
    }
}


fun uriToBase64(context: Context, uri: Uri): String {
    val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
    val byteArray = outputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

fun base64ToBitmap(base64Str: String): Bitmap? {
    return try {
        val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
