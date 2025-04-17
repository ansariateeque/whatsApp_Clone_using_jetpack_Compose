@file:Suppress("DEPRECATION")

package com.example.whatsapp.presentation.mainscreen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.whatsapp.presentation.calls.CallsScreen
import com.example.whatsapp.presentation.chat.ChatModel
import com.example.whatsapp.presentation.chat.ChatScreen
import com.example.whatsapp.presentation.chat.ChattingScreen
import com.example.whatsapp.presentation.communities.CommunitiesScreen
import com.example.whatsapp.presentation.floatingactionbutton.UserListScreen
import com.example.whatsapp.presentation.updates.UpdatesScreen
import com.example.whatsapp.presentation.userregisterationscreen.UserModel
import com.google.gson.Gson

@Composable
fun BottomNavigationGraph(navController: NavHostController) {
    val mainViewModel: MainViewModel = hiltViewModel()

    NavHost(navController, startDestination = BottomNavItem.Chats.route) {


        composable(BottomNavItem.Chats.route) {
            ChatScreen(navController, mainViewModel)
        }


        composable(BottomNavItem.Updates.route) { UpdatesScreen(navController) }
        composable(BottomNavItem.Communities.route) { CommunitiesScreen(navController) }
        composable(BottomNavItem.Calls.route) { CallsScreen(navController) }
        composable(BottomNavItem.UserList.route) { UserListScreen(mainViewModel, navController) }

        // For starting new chat with user
        composable(
            route = "${BottomNavItem.ChattingScreen.route}?user={user}",
            arguments = listOf(navArgument("user") {
                type = NavType.StringType
                nullable = true
            })
        ) {
            val userJson = it.arguments?.getString("user")
            val userModel = userJson?.let { json -> Gson().fromJson(json, UserModel::class.java) }

            ChattingScreen(
                navController = navController,
                mainViewModel = mainViewModel,
                userModel = userModel,
                chatModel = null
            )
        }

// For existing chat
        composable(
            route = "${BottomNavItem.ChattingScreen.route}?chatModel={chatModel}",
            arguments = listOf(navArgument("chatModel") {
                type = NavType.StringType
                nullable = true
            })
        ) {
            val chatModelJson = it.arguments?.getString("chatModel")
            val chatModel = chatModelJson?.let { json -> Gson().fromJson(json, ChatModel::class.java) }

            ChattingScreen(
                navController = navController,
                mainViewModel = mainViewModel,
                userModel = null,
                chatModel = chatModel
            )
        }

    }
}