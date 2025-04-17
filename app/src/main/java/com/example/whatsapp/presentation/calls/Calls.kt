package com.example.whatsapp.presentation.calls

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.whatsapp.R
import com.example.whatsapp.presentation.communities.text
import com.example.whatsapp.presentation.mainscreen.BottomNavItem
import com.example.whatsapp.presentation.mainscreen.SearchTopBar
import com.example.whatsapp.presentation.mainscreen.topAppBar

@Composable
fun CallsScreen(navController: NavHostController) {


    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    var isSearchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

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
                topAppBar(BottomNavItem.Calls.route, color = Color.Black, onCameraClick = {}, onSearchClick = {
                    if (currentRoute == it) {
                        isSearchActive = true
                    }
                }, onMoreClick = {})
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Favorites",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 16.dp),
            color = Color.Black
        )

        FavoriteSection()

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {}, colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.dark_green),
            ), modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ) {
            Text(text = "Start a new call", fontSize = 20.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Recent Calls",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 16.dp),
            color = Color.Black
        )


        CallListSection()
    }
}