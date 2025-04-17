package com.example.whatsapp.presentation.floatingactionbutton

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.whatsapp.R
import com.example.whatsapp.presentation.mainscreen.BottomNavItem
import com.example.whatsapp.presentation.mainscreen.ContactModel
import com.example.whatsapp.presentation.mainscreen.MainViewModel
import com.example.whatsapp.presentation.mainscreen.base64ToBitmap
import com.example.whatsapp.presentation.userregisterationscreen.UserModel
import com.google.gson.Gson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(
    mainViewModel: MainViewModel,
    navController: NavController,
) {


    val context = LocalContext.current
    LaunchedEffect(Unit) {
        mainViewModel.fetchDeviceContacts(context)
    }

    val whatsappUsers_plusMyContact by mainViewModel.firebaseUsersInContacts.collectAsState()

    val inviteContacts by mainViewModel.nonWhatsAppContacts.collectAsState()

    val currentuser = mainViewModel.currentUser.collectAsState()

    Log.d("userlist", whatsappUsers_plusMyContact.toString())
    Scaffold(topBar = {
        UserListTopAppBar(
            whatsappUsers_plusMyContact.size - 1 + inviteContacts.size,
            onBackClick = {
                navController.popBackStack()
            })
    }) {
        Column(modifier = Modifier.fillMaxSize().padding(it)) {

            LazyColumn(modifier = Modifier.fillMaxSize()) {

                // WhatsApp users list
                items(whatsappUsers_plusMyContact) { user ->
                    UserItem(user = user, currentUserID = currentuser.value?.userId, onClick = {
                        val userJson = Uri.encode(Gson().toJson(user))
                        navController.navigate("${BottomNavItem.ChattingScreen.route}?user=$userJson") {
                            navController.popBackStack()
                        }
                    })
                }

                // Divider between two lists
                if (inviteContacts.isNotEmpty()) {
                    item {
                        HorizontalDivider(
                            color = Color.Gray,
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    item {
                        Text(text = "Invite to WhatsApp", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    }
                }

                // Non-WhatsApp users list
                items(inviteContacts) { contact ->
                    InviteContactItem(
                        contact = contact,
                        onInviteClick = {
                            // Invite button click action
                        }
                    )
                }
            }

        }
        }

}

@Composable
fun InviteContactItem(
    contact: ContactModel,
    onInviteClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left - Default person icon
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
                .padding(8.dp)
        )

        // Middle - Contact Name
        Text(
            text = contact.name,
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        // Right - Invite Button
        Button(
            onClick = onInviteClick,
            shape = RoundedCornerShape(20.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
        ) {
            Text("Invite")
        }
    }

}

@Composable
fun UserItem(user: UserModel, currentUserID: String?, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (user.profileimage.isEmpty()) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Default Profile",
                tint = Color.Black,
                modifier = Modifier.size(50.dp).padding(5.dp).clip(CircleShape)
                    .background(Color.LightGray),

                )
        } else {
            val bitmapImage= base64ToBitmap(user.profileimage)
            bitmapImage.let {

                if (it != null) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
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

        Column {
            Text(
                text = if (currentUserID == user.userId) "${user.name} (You)" else user.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = user.status, style = MaterialTheme.typography.bodySmall, color = Color.Gray
            )
        }
    }
}


@Composable
fun UserListTopAppBar(
    contactCount: Int,
    onBackClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onMoreClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back Arrow
        Icon(painter = painterResource(R.drawable.arrowback),
            contentDescription = "Back",
            modifier = Modifier.size(24.dp).clickable { onBackClick() })

        Spacer(modifier = Modifier.width(16.dp))

        // Title + Count Column
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Select Contact",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "$contactCount contacts",
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall
            )
        }

        // Search Icon
        Icon(painter = painterResource(id = R.drawable.search),
            contentDescription = "Search",
            modifier = Modifier.padding(horizontal = 8.dp).size(24.dp)
                .clickable { onSearchClick() })

        // More Icon
        Icon(painter = painterResource(id = R.drawable.more), // use your ⋮ icon here
            contentDescription = "More",
            modifier = Modifier.size(24.dp).clickable { onMoreClick() })
    }
}

