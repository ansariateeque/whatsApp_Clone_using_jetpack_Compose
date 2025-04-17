package com.example.whatsapp.presentation.chat


import android.view.View
import android.view.ViewTreeObserver
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.whatsapp.R
import com.example.whatsapp.presentation.mainscreen.MainViewModel
import com.example.whatsapp.presentation.mainscreen.base64ToBitmap
import com.example.whatsapp.presentation.mainscreen.uriToBase64
import com.example.whatsapp.presentation.userregisterationscreen.UserModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChattingScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
    userModel: UserModel?,
    chatModel: ChatModel?,
) {
    val context = LocalContext.current
    val currentUser = mainViewModel.currentUser.collectAsState()

    var sendTextMsg by remember { mutableStateOf("") }

    val listState = rememberLazyListState()

    val isKeyboardVisible = isKeyboardOpen()

    val receieverId = userModel?.userId ?: chatModel?.receiverId
    val recievername = userModel?.name ?: chatModel?.receiverName
    val recieverprofileimage = userModel?.profileimage ?: chatModel?.receiverProfileImage

    val messages by mainViewModel.chatMessages.collectAsState()

    LaunchedEffect(messages.size, sendTextMsg, isKeyboardVisible) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    LaunchedEffect(Unit) {
        val senderId = currentUser.value?.userId
        val receiverId = receieverId
        val senderRoom = senderId + receiverId
        mainViewModel.startListeningForMessages(senderRoom)
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // 🔝 Top App Bar - STAYS FIXED
        TopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (recieverprofileimage!!.isEmpty()) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Default Profile",
                            tint = Color.Black,
                            modifier = Modifier
                                .size(42.dp)
                                .padding(8.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray),
                        )
                    } else {
                        val bitImage = base64ToBitmap(recieverprofileimage)
                        bitImage.let {
                            if (it != null) {
                                Image(
                                    bitmap = it.asImageBitmap(),
                                    contentDescription = "Profile Image",
                                    modifier = Modifier.size(36.dp).clip(CircleShape)
                                        .background(Color.LightGray),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }

                    }

                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = if (currentUser.value?.userId == receieverId) "${recievername!!} (You)" else recievername!!,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(1.dp))
                        Text(
                            text = "Online",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Green,
                            fontSize = 12.sp
                        )
                    }
                }
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(R.drawable.arrowback),
                        contentDescription = "Back"
                    )
                }
            },
            actions = {
                IconButton(onClick = { /* Video Call */ }) {
                    Icon(
                        painter = painterResource(R.drawable.video),
                        contentDescription = "Video Call",
                        modifier = Modifier.size(24.dp)
                    )
                }
                IconButton(onClick = { /* Audio Call */ }) {
                    Icon(
                        painter = painterResource(R.drawable.outline_phone_24),
                        contentDescription = "Call",
                        modifier = Modifier.size(24.dp)
                    )
                }
                IconButton(onClick = { /* More Options */ }) {
                    Icon(
                        painter = painterResource(R.drawable.more),
                        contentDescription = "More",
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
        )


        // 💬 Chat Messages Area
        Column(
            modifier = Modifier
                .padding(top = 64.dp, bottom = 72.dp) // adjust for topBar + bottomBar height
                .fillMaxSize()
                .background(Color(0xFFF0F0F0))
        ) {

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                items(messages) { msg ->
                    val isSender = msg.senderId == currentUser.value?.userId

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = if (isSender) Arrangement.End else Arrangement.Start
                    ) {
                        MessageBubble(
                            message = msg.message,
                            time = msg.timestamp,
                            isSender = isSender
                        )
                    }

                }


            }
        }

        // ⌨️ Chat Input Box — Moves with Keyboard
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .imePadding() // make it move up with keyboard
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = sendTextMsg,
                onValueChange = { sendTextMsg = it },
                placeholder = {
                    Text(
                        text = "Type a message",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = MaterialTheme.shapes.medium,
                singleLine = true
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = {
                if (sendTextMsg.isNotBlank()) {
                    val senderId = currentUser.value?.userId
                    val receiverId = receieverId
                    val senderRoom = senderId + receiverId
                    val receiverRoom = receiverId + senderId
                    val senderName = currentUser.value?.name
                    val receiverName = recievername
                    val receiverProfileImage = recieverprofileimage

                    mainViewModel.sendMessageToFirebase(
                        message = sendTextMsg,
                        senderRoom = senderRoom,
                        receiverRoom = receiverRoom,
                        receiverId = receiverId!!,
                        senderName = senderName!!,
                        receiverName = receiverName!!,
                        recieverProfileImage = receiverProfileImage!!
                    )

                    sendTextMsg = ""
                }
            }) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

        }
    }

}

@Composable
fun MessageBubble(
    message: String,
    time: Long,
    isSender: Boolean
) {
    val formattedTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(time))
    val maxWidth = LocalConfiguration.current.screenWidthDp.dp * 0.7f

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isSender) colorResource(R.color.chat_light_green) else Color.White,
        modifier = Modifier
            .wrapContentWidth()
            .widthIn(max = maxWidth)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = message,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = formattedTime,
                color = Color.Gray,
                fontSize = 10.sp,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}




@Composable
fun isKeyboardOpen(): Boolean {
    val rootView = LocalView.current
    val keyboardState = remember { mutableStateOf(false) }

    DisposableEffect(rootView) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val r = android.graphics.Rect()
            rootView.getWindowVisibleDisplayFrame(r)
            val screenHeight = rootView.rootView.height
            val keypadHeight = screenHeight - r.bottom
            keyboardState.value = keypadHeight > screenHeight * 0.15
        }
        rootView.viewTreeObserver.addOnGlobalLayoutListener(listener)
        onDispose {
            rootView.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }

    return keyboardState.value
}
