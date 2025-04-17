package com.example.whatsapp.presentation.updates

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.whatsapp.R
import com.example.whatsapp.presentation.mainscreen.BottomNavItem
import com.example.whatsapp.presentation.mainscreen.SearchTopBar
import com.example.whatsapp.presentation.mainscreen.topAppBar

@Composable
fun UpdatesScreen(navController: NavHostController) {


    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    var isSearchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    var showMenu by remember { mutableStateOf(false) }


    val dummystatus = listOf(
        StatusModel("Salman Khan", " 5 min ago", painterResource(id = R.drawable.salmankhan)),
        StatusModel("ShahRukh Khan", "50 min ago", painterResource(id = R.drawable.sharukhkhan))
    )


    Column(modifier = Modifier.fillMaxSize()) {
        if (isSearchActive) {
            SearchTopBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                onClose = {
                    isSearchActive = false
                    searchQuery = ""
                }
            )

        } else {
            if (currentRoute != null) {
                topAppBar(BottomNavItem.Updates.route, color = Color.Black, onCameraClick = {}, onSearchClick = {
                    if (currentRoute == it) {
                        isSearchActive = true
                    }
                }, onMoreClick = {
                    if (currentRoute == it) {
                        showMenu = true
                    }
                }, showMenu = showMenu, onDismissMenu = { showMenu = false })
            }
        }



        MyStatusSection { }

        Text(
            text = "Recent Updates",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        if (dummystatus.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                    .then(Modifier.sizeIn(maxHeight = 300.dp))
            ) {
                items(dummystatus) {
                    StatusItem(status = it)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color.Gray)
        }

        Text(
            text = "Channel",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(modifier = Modifier.padding(horizontal = 24.dp)) {

            Text(text = "stay updated on the topics that matter to you. Find channel to follow below", color = Color.Gray)

            Spacer(modifier = Modifier.height(32.dp))

            Text(text = "Find channel to follows", color = Color.Gray)
        }
        Spacer(modifier = Modifier.height(16.dp))

        ChannelListSection()


    }
}



