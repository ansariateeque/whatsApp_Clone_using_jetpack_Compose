package com.example.whatsapp.presentation.updates

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun MyStatusSection(onAddClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            Image(
                painter = painterResource(id = R.drawable.salmankhan), // apna profile image
                contentDescription = null,
                modifier = Modifier
                    .size(55.dp)
                    .clip(CircleShape)
            )
            Icon(
                painter = painterResource(R.drawable.baseline_add_24),
                contentDescription = "Add",
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(16.dp)
                    .background(
                        color = colorResource(R.color.light_green),
                        shape = RoundedCornerShape(12.dp),
                    ).padding(2.dp)

            )
        }

        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text("My Status", fontWeight = FontWeight.SemiBold)
            Text("Tap to add status update", fontSize = 12.sp, color = Color.Gray)
        }
    }
}
