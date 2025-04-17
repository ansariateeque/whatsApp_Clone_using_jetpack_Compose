package com.example.whatsapp.presentation.chat

data class MessageModel(
    val senderId:String="",
    val senderrecieverId:String="",
    val senderrecievername:String="",
    val receiverId: String = "",
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

