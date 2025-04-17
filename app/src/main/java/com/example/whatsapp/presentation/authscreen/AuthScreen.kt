package com.example.whatsapp.presentation.authscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController

@Composable
fun AuthScreen() {

    var controller = rememberNavController()

    Scaffold {
        Column(modifier = Modifier.fillMaxSize().padding(it)) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                AuthNavigationGraph(controller)
            }

        }
    }

}