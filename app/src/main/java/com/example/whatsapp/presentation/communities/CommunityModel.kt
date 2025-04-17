package com.example.whatsapp.presentation.communities

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.painter.Painter

data class CommunityModel(
    @DrawableRes val imageRes: Int,
    val communityname:String,
    val member:Int
)

