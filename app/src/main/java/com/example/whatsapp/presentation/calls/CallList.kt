package com.example.whatsapp.presentation.calls

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
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
fun CallItem(call: CallModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Image
        Image(
            painter = painterResource(id = call.imageRes),
            contentDescription = call.name,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
        )

        // Name & Time Column
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        ) {
            Text(
                text = call.name,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
            Text(
                text = call.time,
                fontSize = 13.sp,
                color = Color.Gray
            )
        }

        // Right Icon
        Icon(
            painter = painterResource(
                id = if (call.isVideoCall) R.drawable.video else R.drawable.add_call
            ),
            contentDescription = if (call.isVideoCall) "Video Call" else "Voice Call",
            tint = colorResource(id = R.color.light_green),
            modifier = Modifier.size(24.dp)
        )
    }
}



@Composable
fun CallListSection() {
    val calls = listOf(
        CallModel("salman khan",R.drawable.salmankhan , "Today, 3:45 PM", false),
        CallModel("shahrukh kham", R.drawable.sharukhkhan, "Yesterday, 10:20 AM", true),
        CallModel("bhuvan  bam", R.drawable.bhuvan_bam, "2 April, 7:15 PM", false),
        CallModel("sharadha kapoor", R.drawable.sharadhakapoor, "31 March, 8:00 AM", true)
    )

    LazyColumn {
        items(calls) { call ->
            CallItem(call = call)
        }
    }
}
