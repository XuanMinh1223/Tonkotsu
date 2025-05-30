package com.nightfire.tonkotsu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.nightfire.tonkotsu.navigation.Screen // Import your Screen sealed class
import com.nightfire.tonkotsu.navigation.addAnimeDetailScreen // Import the extension from navigation module
import com.nightfire.tonkotsu.navigation.addHomeGraph // Import the extension from navigation module
import dagger.hilt.android.AndroidEntryPoint // If you're using Hilt

@Composable
fun TonkotsuNavHost() {
    val navController = rememberNavController() // Remember the NavController instance

    NavHost(
    navController = navController,
    startDestination = Screen.HomeGraph.route, // Set your HomeGraph as the starting point
    modifier = Modifier.fillMaxSize()
    ) {
        // --- Modular Navigation Graph Building ---
        addHomeGraph(navController = navController)

        // Adds the Anime Detail screen
        addAnimeDetailScreen()

        // You can add more feature graphs here as your app grows:
    }
}