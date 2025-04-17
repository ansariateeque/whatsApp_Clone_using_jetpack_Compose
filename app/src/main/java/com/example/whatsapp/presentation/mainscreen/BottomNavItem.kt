package com.example.whatsapp.presentation.mainscreen
import androidx.annotation.DrawableRes
import com.example.whatsapp.R


sealed class BottomNavItem(val route: String, @DrawableRes val iconRes: Int, val label: String) {
    object Chats : BottomNavItem("Chats", R.drawable.chat_icon, "Chats")
    object Updates : BottomNavItem("Updates",  R.drawable.update_icon, "Updates")
    object Communities : BottomNavItem("Communities", R.drawable.communities_icon, "Communities")
    object Calls : BottomNavItem("Calls",  R.drawable.telephone, "Calls")
    object UserList:BottomNavItem("UserList",0,"UserList")
    object ChattingScreen:BottomNavItem("ChattingScreen",0,"ChattingScreen")
}
