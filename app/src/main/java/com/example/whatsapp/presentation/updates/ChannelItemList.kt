package com.example.whatsapp.presentation.updates

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
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
fun ChannelItemWithFollow(
    imageRes: Int,
    channelName: String,
    lastMessage: String,
    isFollowing: Boolean = false,
    onFollowClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Square Image
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier
                .size(55.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        // Name & Message
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        ) {
            Text(
                text = channelName,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
            Text(
                text = lastMessage,
                fontSize = 13.sp,
                color = Color.Gray
            )
        }

        // Follow Button
        Text(
            text = if (isFollowing) "Following" else "Follow",
            color = if (isFollowing) Color.Gray else colorResource(R.color.light_green),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(
                    if (isFollowing) Color.LightGray.copy(alpha = 0.3f)
                    else colorResource(R.color.light_green).copy(alpha = 0.15f)
                )
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .clickable { onFollowClick() }
        )
    }
}


@Composable
fun ChannelListSection() {

    val channels = remember {       mutableStateListOf(
            ChannelModel(
                name = "Neat Roots",
                imageRes = R.drawable.neat_roots,
                lastMessage = "Latest news in Tech",
                isFollowing = false
            ),
            ChannelModel(
                name = "Food Lovers",
                imageRes = R.drawable.img,
                lastMessage = "Latest news in Tech",
                isFollowing = false
            ),
        )
    }



    LazyColumn {
        items(channels) { channel ->
            ChannelItemWithFollow(
                imageRes = channel.imageRes,
                channelName = channel.name,
                lastMessage = channel.lastMessage,
                isFollowing = channel.isFollowing,
                onFollowClick = {
                    val index = channels.indexOf(channel)
                    if (index != -1) {
                        channels[index] = channel.copy(isFollowing = !channel.isFollowing)
                    }
                }

            )
        }
    }
}
