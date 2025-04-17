package com.example.whatsapp.presentation.chat

import android.os.Parcelable
import androidx.compose.ui.graphics.painter.Painter
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatModel(
    val senderId: String = "",
    val receiverId: String = "",
    val senderName: String = "",
    val receiverName: String = "",
    val receiverProfileImage: String = "", // 👈 NEW
    val lastMessage: String = "",
    val timestamp: Long = System.currentTimeMillis(),
) : Parcelable
