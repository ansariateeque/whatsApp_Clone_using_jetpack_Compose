package com.example.whatsapp.presentation.chat

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.example.whatsapp.R
import com.example.whatsapp.presentation.mainscreen.BottomNavItem
import com.example.whatsapp.presentation.mainscreen.MainViewModel
import com.example.whatsapp.presentation.mainscreen.SearchTopBar
import com.example.whatsapp.presentation.mainscreen.base64ToBitmap
import com.example.whatsapp.presentation.mainscreen.formatChatTimestamp
import com.example.whatsapp.presentation.mainscreen.topAppBar
import com.google.gson.Gson

@Composable
fun ChatScreen(navController: NavHostController, mainViewModel: MainViewModel) {

    val context = LocalContext.current
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val chatList by mainViewModel.chatList.collectAsState()
    Log.d("chatlist", chatList.toString())

    var isSearchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var isSearchSubmitted by remember { mutableStateOf(false) }


    val filteredChatList = if (isSearchActive && searchQuery.isNotBlank()) {
        chatList.filter {
            it.receiverName.contains(searchQuery, ignoreCase = true)
        }
    } else {
        chatList
    }

    LaunchedEffect(isSearchSubmitted, filteredChatList) {
        if (isSearchSubmitted) {
            if (filteredChatList.isEmpty()) {
                Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show()
            }
            isSearchSubmitted = false // Reset after handling
        }
    }


    Column(modifier = Modifier.fillMaxSize()) {
        if (isSearchActive) {
            SearchTopBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                onClose = {
                    isSearchActive = false
                    searchQuery = ""
                    isSearchSubmitted = false
                }, onSearchSubmit = {
                    isSearchSubmitted = true
                }
            )

        } else {
            if (currentRoute != null) {
                topAppBar(
                    BottomNavItem.Chats.route,
                    color = colorResource(R.color.light_green),
                    onCameraClick = {},
                    onSearchClick = {
                        if (currentRoute == it) {
                            isSearchActive = true
                        }
                    },
                    onMoreClick = {})
            }
        }
        LazyColumn {
            items(chatList) { chat ->
                ChatListItem(
                    profileImage = chat.receiverProfileImage,
                    name = chat.receiverName,
                    lastMessage = chat.lastMessage,
                    time = chat.timestamp,
                    currentuserId = chat.senderId,
                    receiverId = chat.receiverId,
                    onclick = {
                        val chatjson = Uri.encode(Gson().toJson(chat))
                        navController.navigate("${BottomNavItem.ChattingScreen.route}?chatModel=$chatjson")

                    })
            }
        }
    }
}


@Composable
fun ChatListItem(
    profileImage: String,
    name: String,
    lastMessage: String,
    time: Long,
    onclick: () -> Unit,
    currentuserId: String,
    receiverId: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp).clickable { onclick() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Circular Image
        if (profileImage.isEmpty()) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Default Profile",
                tint = Color.Black,
                modifier = Modifier.size(52.dp).padding(5.dp).clip(CircleShape)
                    .background(Color.LightGray),
            )
        } else {
            val bitmapImage = base64ToBitmap(profileImage)
            bitmapImage.let {

                if (it != null) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray)
                            .border(1.dp, Color.Gray, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "Profile Image",
                            modifier = Modifier.fillMaxSize().clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }

                }
            }

        }


        Spacer(modifier = Modifier.width(12.dp))

        // Name and Last Message
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = if (currentuserId == receiverId) "$name (You)" else name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = lastMessage,
                color = Color.Gray,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Time
        Text(
            text = formatChatTimestamp(time),
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}




