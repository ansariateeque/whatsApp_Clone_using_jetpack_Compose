package com.example.whatsapp.presentation.userregisterationscreen

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class UserModel(
    val userId:String="",
    val phoneNUmber:String="",
    val name:String="",
    val status:String="",
    val profileimage:String="",
    val isProfileComplete: Boolean=false
):Parcelable