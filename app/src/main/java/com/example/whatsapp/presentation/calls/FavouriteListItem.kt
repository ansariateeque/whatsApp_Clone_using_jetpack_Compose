package com.example.whatsapp.presentation.calls

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeCompilerApi
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.whatsapp.R


@Composable
fun FavoriteItem(contact: FavoriteContact) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Image(
            painter = painterResource(id = contact.imageRes),
            contentDescription = contact.name,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
        )
        Text(
            text = contact.name,
            fontSize = 13.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}


@Composable
fun FavoriteSection() {
    val favorites = listOf(
        FavoriteContact("Salman Khan", R.drawable.salmankhan),
        FavoriteContact("sharukh khan", R.drawable.sharukhkhan),
        FavoriteContact("sharadha kapoor", R.drawable.sharadhakapoor),
        FavoriteContact("rashmika", R.drawable.rashmika),
        FavoriteContact("rajkummar rao", R.drawable.rajkummar_rao),
        FavoriteContact("disha patani", R.drawable.disha_patani),
        FavoriteContact("bhuvan bam", R.drawable.bhuvan_bam),
        FavoriteContact("carryminati", R.drawable.carryminati)
    )

    LazyRow(
        modifier = Modifier.padding(top = 8.dp, start = 12.dp)
    ) {
        items(favorites) { contact ->
            FavoriteItem(contact = contact)
        }
    }
}

