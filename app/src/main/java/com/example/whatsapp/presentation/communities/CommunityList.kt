package com.example.whatsapp.presentation.communities

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.whatsapp.R
import com.example.whatsapp.presentation.chat.ChatListItem

@Composable
fun CommunityListItem(
    img: Int,
    communityname: String,
    member: Int,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Square Image
        Image(
            painter = painterResource(id = img),
            contentDescription = null,
            modifier = Modifier
                .size(55.dp)
                .clip(CircleShape)
        )

        // Name & Message
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        ) {
            Text(
                text = communityname,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
            Text(
                text = "$member member",
                fontSize = 13.sp,
                color = Color.Gray
            )
        }


    }
}


@Composable
fun CommunityListScreen() {
    val dummyCommunity = listOf(
        CommunityModel(R.drawable.img, "Tech Enthusiasts", 256),
        CommunityModel(R.drawable.img, "Photography Lovers", 128),
        CommunityModel(R.drawable.img, "Travelers United", 512)

    )

    LazyColumn {
        items(dummyCommunity) {
            CommunityListItem(
                img = it.imageRes,
                communityname = it.communityname,
                member = it.member
            )
        }
    }
}


@Composable
@Preview(showBackground = true, showSystemUi = true)
fun text() {
    CommunityListItem(R.drawable.img, "Tech Enthusiasts", 256)
}