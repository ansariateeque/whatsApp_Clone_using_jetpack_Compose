package com.example.whatsapp.presentation.updates

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.whatsapp.R

@Composable
fun StatusItem(status: StatusModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = status.profileImage,
            contentDescription = null,
            modifier = Modifier
                .size(55.dp)
                .clip(CircleShape)
                .border(2.dp, colorResource(R.color.light_green), CircleShape)
        )

        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text(status.name, fontWeight = FontWeight.Medium)
            Text(status.time, fontSize = 12.sp, color = Color.Gray)
        }
    }
}
