package com.example.whatsapp.presentation.updates

import androidx.annotation.DrawableRes

data class ChannelModel(
    val id: Int=0,
    val name: String,
    val lastMessage: String,
    @DrawableRes val imageRes: Int,
    var isFollowing: Boolean
)
