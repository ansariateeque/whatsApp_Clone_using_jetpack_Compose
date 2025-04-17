package com.example.whatsapp.presentation.updates

import androidx.compose.ui.graphics.painter.Painter

data class StatusModel(
    val name: String,
    val time: String,
    val profileImage: Painter
)
